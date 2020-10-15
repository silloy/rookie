package me.silloy.easyexcel;

import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.BaseRowModel;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import lombok.Data;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author shaohuasu
 * @date 2019-02-27 21:01
 * @since 1.8
 */
public class ReadTest {

    public final static String path = "/Users/shaohuasu/Desktop/运营/201903/社区统计0123-0225-1.xlsx";

    @Test
    public void simpleReadListStringV2007() throws IOException {
        InputStream inputStream = new FileInputStream(new File(path));
        try {
            // 解析每行结果在listener中处理
            ExcelListener l = new ExcelListener();
            ExcelReader excelReader = new ExcelReader(inputStream, ExcelTypeEnum.XLSX, null, l);
//            ExcelReader excelReader = new ExcelReader(inputStream, null, l);
            excelReader.read();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    class ExcelListener extends AnalysisEventListener {
        //自定义用于暂时存储data。
        //可以通过实例获取该值
        private List<Object> datas = new ArrayList<>();

        public void invoke(Object object, AnalysisContext context) {
            System.out.println("当前行：" + context.getCurrentRowNum());
            System.out.println(object);
            datas.add(object);//数据存储到list，供批量处理，或后续自己业务逻辑处理。
            doSomething(object);//根据自己业务做处理
        }

        private void doSomething(Object object) {
            //1、入库调用接口
        }

        public void doAfterAllAnalysed(AnalysisContext context) {
            // datas.clear();//解析结束销毁不用的资源
        }

        public List<Object> getDatas() {
            return datas;
        }

        public void setDatas(List<Object> datas) {
            this.datas = datas;
        }
    }



    @Test
    public void reflectModel() throws Exception{
        InputStream inputStream = new FileInputStream(new File(path));
        try {
            AnalysisEventListener listener = new ExcelListener();
            ExcelReader excelReader = new ExcelReader(inputStream, ExcelTypeEnum.XLSX, null, listener, true);
            excelReader.read(new Sheet(0, 1, LoanInfo.class));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Data
    public static class LoanInfo extends BaseRowModel {
        @ExcelProperty(index = 0)
        private String cityName;
        @ExcelProperty(index = 1)
        private String stockName;
        @ExcelProperty(index = 12)
        private String communityName;
        @ExcelProperty(index = 3)
        private String communityId;
        @ExcelProperty(index = 4, format = "yyyy/MM/dd")
        private Date openday;
        @ExcelProperty(index = 5)
        private Integer endDay;
        @ExcelProperty(index = 6)
        private Double gmv;
        @ExcelProperty(index = 7)
        private String re;
        @ExcelProperty(index = 8)
        private Integer ordernum;
        @ExcelProperty(index = 9)
        private Integer mem;
        @ExcelProperty(index = 10)
        private String chargeMan;

        public Long getCommunityId() {
            if (Objects.isNull(communityId)) {
                return null;
            }
            return Long.valueOf(communityId.trim());
        }


//        @ExcelProperty(index = 2,format = "yyyy/MM/dd")
//        private Date loanDate;

//        @ExcelProperty(value = {"一级表头","二级表头"})
//        private BigDecimal sax;
    }


    /**
     * 07版本excel读数据量少于1千行数据自动转成javamodel，内部采用回调方法.
     *
     * @throws IOException 简单抛出异常，真实环境需要catch异常,同时在finally中关闭流
     */
//    @Test
//    public void simpleReadJavaModelV2007() throws IOException {
//        InputStream inputStream = new FileInputStream(new File(path));
//        List<Object> data = EasyExcelFactory.read(inputStream, new Sheet(2, 1, ReadModel.class));
//        inputStream.close();
//        print(data);
//    }
//
//    /**
//     * 07版本excel读数据量大于1千行，内部采用回调方法.
//     *
//     * @throws IOException 简单抛出异常，真实环境需要catch异常,同时在finally中关闭流
//     */
//    @Test
//    public void saxReadListStringV2007() throws IOException {
//        InputStream inputStream = new FileInputStream(new File(path));
//        ExcelListener excelListener = new ExcelListener();
//        EasyExcelFactory.readBySax(inputStream, new Sheet(1, 1), excelListener);
//        inputStream.close();
//
//    }
//    /**
//     * 07版本excel读数据量大于1千行，内部采用回调方法.
//     *
//     * @throws IOException 简单抛出异常，真实环境需要catch异常,同时在finally中关闭流
//     */
//    @Test
//    public void saxReadJavaModelV2007() throws IOException {
//        InputStream inputStream = new FileInputStream(new File(path));
//        ExcelListener excelListener = new ExcelListener();
//        EasyExcelFactory.readBySax(inputStream, new Sheet(2, 1, ReadModel.class), excelListener);
//        inputStream.close();
//    }
//
//    /**
//     * 07版本excel读取sheet
//     *
//     * @throws IOException 简单抛出异常，真实环境需要catch异常,同时在finally中关闭流
//     */
//    @Test
//    public void saxReadSheetsV2007() throws IOException {
//        InputStream inputStream = new FileInputStream(new File(path));
//        ExcelListener excelListener = new ExcelListener();
//        ExcelReader excelReader = EasyExcelFactory.getReader(inputStream,excelListener);
//        List<Sheet> sheets = excelReader.getSheets();
//        System.out.println("llll****"+sheets);
//        System.out.println();
//        for (Sheet sheet:sheets) {
//            if(sheet.getSheetNo() ==1) {
//                excelReader.read(sheet);
//            }else if(sheet.getSheetNo() ==2){
//                sheet.setHeadLineMun(1);
//                sheet.setClazz(ReadModel.class);
//                excelReader.read(sheet);
//            }else if(sheet.getSheetNo() ==3){
//                sheet.setHeadLineMun(1);
//                sheet.setClazz(ReadModel2.class);
//                excelReader.read(sheet);
//            }
//        }
//        inputStream.close();
//    }
//
//
//
//    /**
//     * 03版本excel读数据量少于1千行数据，内部采用回调方法.
//     *
//     * @throws IOException 简单抛出异常，真实环境需要catch异常,同时在finally中关闭流
//     */
//    @Test
//    public void simpleReadListStringV2003() throws IOException {
//        InputStream inputStream = new FileInputStream(new File(path));
//        List<Object> data = EasyExcelFactory.read(inputStream, new Sheet(1, 0));
//        inputStream.close();
//        print(data);
//    }
//
//    /**
//     * 03版本excel读数据量少于1千行数据转成javamodel，内部采用回调方法.
//     *
//     * @throws IOException 简单抛出异常，真实环境需要catch异常,同时在finally中关闭流
//     */
//    @Test
//    public void simpleReadJavaModelV2003() throws IOException {
//        InputStream inputStream = new FileInputStream(new File(path));
//        List<Object> data = EasyExcelFactory.read(inputStream, new Sheet(2, 1, ReadModel.class));
//        inputStream.close();
//        print(data);
//    }
//
//    /**
//     * 03版本excel读数据量大于1千行数据，内部采用回调方法.
//     *
//     * @throws IOException 简单抛出异常，真实环境需要catch异常,同时在finally中关闭流
//     */
//    @Test
//    public void saxReadListStringV2003() throws IOException {
//        InputStream inputStream = new FileInputStream(new File(path));
//        ExcelListener excelListener = new ExcelListener();
//        EasyExcelFactory.readBySax(inputStream, new Sheet(2, 1), excelListener);
//        inputStream.close();
//    }
//
//    /**
//     * 03版本excel读数据量大于1千行数据转成javamodel，内部采用回调方法.
//     *
//     * @throws IOException 简单抛出异常，真实环境需要catch异常,同时在finally中关闭流
//     */
//    @Test
//    public void saxReadJavaModelV2003() throws IOException {
//        InputStream inputStream = new FileInputStream(new File(path));
//        ExcelListener excelListener = new ExcelListener();
//        EasyExcelFactory.readBySax(inputStream, new Sheet(2, 1, ReadModel.class), excelListener);
//        inputStream.close();
//    }
//
//    /**
//     * 00版本excel读取sheet
//     *
//     * @throws IOException 简单抛出异常，真实环境需要catch异常,同时在finally中关闭流
//     */
//    @Test
//    public void saxReadSheetsV2003() throws IOException {
//        InputStream inputStream = new FileInputStream(new File(path));
//        ExcelListener excelListener = new ExcelListener();
//        ExcelReader excelReader = EasyExcelFactory.getReader(inputStream,excelListener);
//        List<Sheet> sheets = excelReader.getSheets();
//        System.out.println();
//        for (Sheet sheet:sheets) {
//            if(sheet.getSheetNo() == 1) {
//                excelReader.read(sheet);
//            }else {
//                sheet.setHeadLineMun(2);
//                sheet.setClazz(ReadModel.class);
//                excelReader.read(sheet);
//            }
//        }
//        inputStream.close();
//    }
    public void print(List<Object> datas) {
        int i = 0;
        for (Object ob : datas) {
            System.out.println(i++);
            System.out.println(ob);
        }
    }
}
