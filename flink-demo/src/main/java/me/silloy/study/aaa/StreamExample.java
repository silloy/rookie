//package me.silloy.study.aaa;
//
//import com.alibaba.fastjson.JSONObject;
//import org.apache.flink.api.common.functions.MapFunction;
//import org.apache.flink.api.common.serialization.SimpleStringSchema;
//import org.apache.flink.streaming.api.datastream.DataStream;
//import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
//import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011;
//import org.apache.flink.table.api.java.StreamTableEnvironment;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.sql.*;
//import java.util.Properties;
//
///**
// * @author: lizelong
// * @date: 2019/11/4
// * @description:
// **/
//public class StreamExample {
//
//    private static StreamTableEnvironment tEnv;
//
//    private static final Logger LOG = LoggerFactory.getLogger(UserBehaviorEventTracker.class);
//
//    public static void main(String[] args) {
//        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
//        tEnv = StreamTableEnvironment.create (env);
//
//        Properties properties = new Properties();
//        properties.setProperty("bootstrap.servers","10.10.10.49:9092");
//        properties.setProperty("group.id", "test");
//        DataStream stream = env
//                .addSource(new FlinkKafkaConsumer011<>("user_behavior", new SimpleStringSchema (), properties));
//        stream.map ((MapFunction<String, String>)(value)->{
//            JSONObject obj = JSONObject.parseObject (value);
//            String userId = obj.getString ("user_id");
//            if (qRule(userId).equals ("1")){
//                LOG.info ("log rule is 1");
//                return "rule is 1";
//            } else if(qRule(userId).equals ("2")) {
//                LOG.info ("log rule is 2");
//                return "rule is change";
//            }
//            return "123";
//        }).print ();
////        stream.keyBy ("user_id").sum ("pv");
////        SingleOutputStreamOperator tr = stream.flatMap ((FlatMapFunction<String, String>) (value, out) -> {
////            for (String word : value.split (",")) {
////                out.collect (word);
////            }
////        });
////        tr.print ();
////        tr.addSink (new FlinkKafkaProducer<String> ("10.10.10.49:9092", "stream_sink", new SimpleStringSchema ()));
//
//        try {
//            tEnv.execute ("stream-job");
//        } catch (Exception e) {
//            e.printStackTrace ();
//        }
//    }
//
//    private static String qRule(String userId) {
//        Connection conn = null;
//        PreparedStatement stmt = null;
//        ResultSet rs = null;
//        String rule_sql = "select rule from pvuv_sink_rule where user_id = "+userId;
//
//        try {
//            conn = DriverManager.getConnection("jdbc:mysql://10.10.10.43:3306/geek_dmp_api", "root", "jieke123");
//            stmt = conn.prepareStatement(rule_sql);
//            rs = stmt.executeQuery();
//            while (rs.next()) {
//                String rule = rs.getString ("rule");
//                return rule;
//            }
//        } catch (SQLException e) {
//            e.printStackTrace ();
//        } finally {
//            try {
//                rs.close();
//                stmt.close();
//                conn.close();
//            } catch (SQLException e) {
//                e.printStackTrace ();
//            }
//        }
//        return "";
//    }
//}
