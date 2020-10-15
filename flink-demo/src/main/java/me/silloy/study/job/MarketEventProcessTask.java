//package me.silloy.study.job;
//
//import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
//import org.apache.flink.table.api.TableEnvironment;
//import org.apache.flink.table.api.java.StreamTableEnvironment;
//import org.apache.flink.table.sources.CsvTableSource;
//import org.apache.flink.table.sources.TableSource;
//
//import java.util.Properties;
//
///**
// * @author shaohuasu
// * @date 2019/12/11 3:03 PM
// * @since 1.8
// */
//public class MarketEventProcessTask {
//
//    public static void main(String[] args) {
//        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
//
//        Properties props = new Properties();
//        props.put("bootstrap.servers", "10.10.10.49:9092");
//        props.put("group.id", "test6");
//        props.put("enable.auto.commit", "true");
//        props.put("auto.commit.interval.ms", "1000");
//        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
//        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
//        props.put("auto.offset.reset", "latest");
//
////        StreamTableEnvironment tableEnv = TableEnvironment.create(env);
//        final StreamTableEnvironment tblEnv = TableEnvironment.create(env);
//
////        tblEnv.registerTable("table1");
//
//        // create a TableSource
////        TableSource csvSource = new CsvTableSource("/path/to/file", ...);
//// register the TableSource as table "CsvTable"
////        tblEnv.registerTableSource("CsvTable", csvSource);
//    }
//}
