package com.zj.web.excel;

import lombok.Data;

import java.util.List;


/**
 * 代表要打印的Excel表格,用于存放要导出为Excel的相关数据
 *
 * @param <T> 代表要打印的数据实体，如User等
 */
@Data
public class ExcelEntity<T> {
    private String sheetName = "Sheet1";//默认生成的sheet名称
    private String header = "";//题头
    private String footer = "";//脚注
    //底下是必须具备的属性
    private String fileName;
    private String[] columnNames;//列名
    private int[] columnWidths;//列宽
    private String[] methodNames;//与列名对应的方法名
    private List<T> entities;//数据实体
    private Integer rowIndexBegin;
    private Class clazz;  // 实体类


    public ExcelEntity(String fileName, Class clazz, List<T> entities) {
        this("sheet1", "", "", fileName, clazz, null, null, entities);
    }

    public ExcelEntity(String fileName, String[] columnNames, String[] methodNames, List<T> entities) {
        this("sheet1", "", "", fileName, null, columnNames, methodNames, entities);
    }

    public ExcelEntity(String fileName, Class clazz, String[] columnNames, String[] methodNames, List<T> entities) {
        this("sheet1", "", "", fileName, clazz, columnNames, methodNames, entities);
    }

    public ExcelEntity(String sheetName, String header, String footer, String fileName, Class clazz,
                       String[] columnNames, String[] methodNames, List<T> entities) {
        this.sheetName = sheetName;
        this.header = header;
        this.footer = footer;
        this.fileName = fileName;
        this.clazz = clazz;
        this.columnNames = columnNames;
        this.methodNames = methodNames;
        this.entities = entities;
    }


    // unused

    public ExcelEntity(String sheetName, String header, String footer, String fileName, String[] columnNames,
                       String[] methodNames, List<T> entities, Integer rowIndexBegin) {
        this.sheetName = sheetName;
        this.header = header;
        this.footer = footer;
        this.fileName = fileName;
        this.columnNames = columnNames;
        this.methodNames = methodNames;
        this.entities = entities;

        this.rowIndexBegin = rowIndexBegin;
        this.columnWidths = new int[columnNames.length];
    }

}
