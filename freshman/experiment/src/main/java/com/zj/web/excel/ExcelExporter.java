package com.zj.web.excel;

import com.google.common.collect.Lists;
import com.zj.web.util.SerializationUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * 一个通用的将List<T>中数据导出为Excel文档的工具类
 *
 * @author zhrb@cec.jmu
 */
public class ExcelExporter {
    /**
     * 创建表格内容
     *
     * @param sheet 表内容开始的行数
     * @throws Exception
     */
    private static <T> void export2003FromMap(Sheet sheet, List<Map<String, String>> contents) throws Exception {
        int rowIndexBegin = 0;
        if (CollectionUtils.isEmpty(contents)) {
            return;
        }
        CellStyle style = getDateCellStyle(sheet);
        List<String> headers = Lists.newArrayList(contents.get(0).keySet());
        int[] columnWidths = new int[headers.size()];
        Row row = sheet.createRow(rowIndexBegin++);
        for (int i = 0; i < headers.size(); i++) {
            Cell cell = row.createCell(i);
            columnWidths[i] = (headers.get(i).getBytes().length + 2) * 256;
            sheet.setColumnWidth(i, columnWidths[i]);
            fillSingleCell(cell, headers.get(i), style);
        }

        for (Map<String, String> map : contents) {
            Row con = sheet.createRow(rowIndexBegin++);
            for (int i = 0; i < headers.size(); i++) {
                Cell cell = con.createCell(i);
                String content = map.getOrDefault(headers.get(i), "");
                int columnWidth = (content.getBytes().length + 2) * 256;
                if (columnWidth > columnWidths[i]) {// 如果实际内容宽度大于对应的表头宽度，则设置为实际内容宽度
                    columnWidths[i] = columnWidth;
                    sheet.setColumnWidth(i, columnWidths[i]);
                }
                fillSingleCell(cell, content, style);
            }
        }
    }

    /**
     * 根据ExcelEntity等参数生成Workbook
     *
     * @param entity
     * @return
     * @throws Exception
     */
    public static <T> Workbook export2007Excel(ExcelEntity<T> entity) throws Exception {
        Workbook workbook = new SXSSFWorkbook();
        if (Objects.isNull(entity.getClazz())) {
            workbook = export2007Excel(entity.getHeader(), entity.getFooter(), entity.getSheetName(), entity.getColumnNames(), entity.getMethodNames(),
                    entity.getEntities());
        } else {
            workbook = export2007Excel(entity.getHeader(), entity.getFooter(), entity.getSheetName(), entity.getClazz(),
                    entity.getEntities());
        }
        return workbook;
    }

    /**
     * 根据给定参数导出Excel文档
     *
     * @param headerTitle 题头
     * @param sheetName
     * @param columnNames 表头名称
     * @param methodNames
     * @param entities
     * @return
     * @throws Exception
     */
    public static <T> Workbook export2007Excel(String headerTitle, String footerTitle, String sheetName, String[] columnNames,
                                               String[] methodNames, List<T> entities) throws Exception {

        Workbook workbook = new SXSSFWorkbook();

        Sheet sheet = initSheet(workbook, sheetName, headerTitle, footerTitle);

        CellStyle style = getDateCellStyle(sheet);
        int[] columnWidths = new int[columnNames.length];
        createTableHeader(sheet, 0, columnNames, columnWidths, style);
        createTableContent(sheet, 1, methodNames, columnWidths, entities, style);
        return workbook;
    }


    private static <T> Workbook export2007Excel(String headerTitle, String footerTitle, String sheetName, Class clazz, List<T> entities) throws Exception {
        Workbook newWorkBook2007 = new SXSSFWorkbook();
        Sheet sheet = initSheet(newWorkBook2007, sheetName, headerTitle, footerTitle);

        Field[] fields = Arrays.stream(clazz.getDeclaredFields())
                .filter(r -> Objects.nonNull(r.getAnnotation(ExcelField.class)))
                .toArray(Field[]::new);

        Integer maxOrdinal = Arrays.stream(fields)
                .mapToInt(f -> f.getAnnotation(ExcelField.class).ordinal()).max().getAsInt();

        CellStyle style = getDateCellStyle(sheet);
        int[] columnWidths = new int[maxOrdinal + 1];
        createTableHeader(sheet, 0, fields, columnWidths, style);
        createTableContent(sheet, 1, fields, columnWidths, entities, style);
        return newWorkBook2007;
    }



    private static Sheet initSheet(Workbook workbook, String sheetName, String headerTitle, String footerTitle) {
        Sheet sheet = workbook.createSheet(sheetName);
        Header header = sheet.getHeader();
        header.setCenter(headerTitle);

        Footer footer = sheet.getFooter();
        footer.setCenter(footerTitle);
        return sheet;
    }




    private static <T> void createTableContent(Sheet sheet, int rowIndexBegin,
                                               Field[] fields, int[] columnWidths, List<T> entities, CellStyle style) throws Exception {
        Class<? extends Object> clazz = null;
        if (entities.size() > 0)
            clazz = entities.get(0).getClass();

        for (T t : entities) {
            Row row = sheet.createRow(rowIndexBegin++);
            for (Field f : fields) {
                int column = f.getAnnotation(ExcelField.class).ordinal();
                String getMethodName = "get"
                        + f.getName().substring(0, 1).toUpperCase()
                        + f.getName().substring(1);
                fillCell(sheet, row, column, getMethodName, clazz, t, columnWidths, style);
            }
        }
    }


    public static  <T> void fillCell(Sheet sheet, Row row, int column, String getMethodName, Class<? extends Object> clazz,
                         T t, int[] columnWidths, CellStyle style) throws Exception {
        Cell cell = row.createCell(column);
        Method method = clazz.getMethod(getMethodName);
        Object object = Optional.ofNullable(method.invoke(t)).orElse("");

        Object content = fillSingleCell(cell, object, style);
        int columnWidth = (Objects.requireNonNull(SerializationUtil.serialize(content)).length + 2) * 128;
        if (columnWidth > columnWidths[column]) {// 如果实际内容宽度大于对应的表头宽度，则设置为实际内容宽度
            columnWidths[column] = columnWidth;
//            sheet.setColumnWidth(column, columnWidths[column]);
//            sheet.autoSizeColumn(column);
        }
    }

    private static Object fillSingleCell(Cell cell, Object object, CellStyle style) {
        Object content = "";
        if (object.getClass().equals(Date.class)) {
            cell.setCellStyle(style);
            cell.setCellValue((Date) object);
        } else if (object.getClass().equals(Double.class) || object.getClass().equals(double.class)) {
            content = BigDecimal.valueOf((double) object).setScale(2, RoundingMode.HALF_UP).doubleValue();
            cell.setCellValue((double) content);
        } else if (object.getClass().equals(Integer.class) || object.getClass().equals(int.class)) {
            content = object;
            if (String.valueOf(object).length() == 8) {
                try {
                    content = new SimpleDateFormat("yyyyMMdd").parse(String.valueOf(object));
                    cell.setCellStyle(style);
                } catch (ParseException e) {
                    cell.setCellValue((int)content);
                }
                cell.setCellValue((Date) content);
            } else {
                cell.setCellValue((int)content);
            }
        } else {
            content = object.toString();
            cell.setCellValue((String) content);
        }
        return content;
    }

    private static CellStyle getDateCellStyle(Sheet sheet) {
        CellStyle style = sheet.getWorkbook().createCellStyle();
        DataFormat format = sheet.getWorkbook().createDataFormat();
        style.setDataFormat(format.getFormat("yyyy/MM/dd"));
        return style;
    }


    private static void createTableHeader(Sheet sheet, int index, Field[] fields,
                                          int[] columnWidths, CellStyle style) {
        Row headerRow = sheet.createRow(index);

        for (Field f : fields) {
            ExcelField info = f.getAnnotation(ExcelField.class);
            int i = info.ordinal();
            String desc = info.desc();
            fillHeaderCell(sheet, headerRow, i, desc, style, columnWidths);
        }
    }

    /**
     * 创建表头
     *
     * @param sheet
     * @param index        表头开始的行数
     * @param columnNames
     * @param columnWidths
     */
    private static void createTableHeader(Sheet sheet, int index, String[] columnNames,
                                          int[] columnWidths, CellStyle style) {

        Row headerRow = sheet.createRow(index);
        for (int i = 0; i < columnNames.length; i++) {
            fillHeaderCell(sheet, headerRow, i, columnNames[i], style, columnWidths);
        }
    }

    private static void fillHeaderCell(Sheet sheet, Row headerRow, int i, String desc, CellStyle style, int[] columnWidths) {
        Cell headerCell = headerRow.createCell(i);
        headerCell.setCellStyle(style);
        headerCell.setCellValue(desc);
        columnWidths[i] = (desc.getBytes().length + 2) * 256;
//        sheet.setColumnWidth(i, columnWidths[i]);
//        sheet.autoSizeColumn(i);
    }




    private static CellStyle fillTableHeader(Sheet sheet) {
        Font font = sheet.getWorkbook().createFont();
        font.setBold(true);
        CellStyle style = sheet.getWorkbook().createCellStyle();
        style.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setFont(font);
        return style;
    }



    /**
     * 创建表格内容
     *
     * @param sheet
     * @param rowIndexBegin 表内容开始的行数
     * @param methodNames   T对象的方法名
     * @param columnWidths
     * @param entities
     * @throws Exception
     */
    private static <T> void createTableContent(Sheet sheet, int rowIndexBegin, String[] methodNames, int[] columnWidths,
                                               List<T> entities, CellStyle style) throws Exception {
        Class<? extends Object> clazz = null;
        if (entities.size() > 0)
            clazz = entities.get(0).getClass();

        for (T t : entities) {
            Row row = sheet.createRow(rowIndexBegin++);
            for (int i = 0; i < methodNames.length; i++) {
                String getMethodName = "get"
                        + methodNames[i].substring(0, 1).toUpperCase()
                        + methodNames[i].substring(1);
                fillCell(sheet, row, i, getMethodName, clazz, t, columnWidths, style);
            }
        }
    }

}