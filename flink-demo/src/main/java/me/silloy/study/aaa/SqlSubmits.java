///*
// * Licensed to the Apache Software Foundation (ASF) under one
// * or more contributor license agreements.  See the NOTICE file
// * distributed with this work for additional information
// * regarding copyright ownership.  The ASF licenses this file
// * to you under the Apache License, Version 2.0 (the
// * "License"); you may not use this file except in compliance
// * with the License.  You may obtain a copy of the License at
// *
// *     http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package me.silloy.study.aaa;
//
//import me.silloy.study.aaa.domain.Event;
//import org.apache.flink.streaming.api.datastream.DataStream;
//import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
//import org.apache.flink.table.api.EnvironmentSettings;
//import org.apache.flink.table.api.Table;
//import org.apache.flink.table.api.java.StreamTableEnvironment;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.sql.*;
//import java.util.HashMap;
//import java.util.Map;
//
//public class SqlSubmits {
//
//    private static final Logger log = LoggerFactory.getLogger(SqlSubmits.class);
//
//    public static void main(String[] args) throws Exception {
//        SqlSubmits submit = new SqlSubmits ();
//        submit.createT ();
//    }
//
//    private StreamTableEnvironment tEnv;
//
//    private void createT() {
//        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
//        EnvironmentSettings settings = EnvironmentSettings.newInstance()
//                .useBlinkPlanner()
//                .inStreamingMode()
//                .build();
//        this.tEnv = StreamTableEnvironment.create(env,settings);
//
////        ClassLoader classLoader = SqlSubmits.class.getClassLoader();
////        URL url = classLoader.getResource("/eden.sql");
////        List<String> sql = null;
////        try {
////            sql = Files.readAllLines (Paths.get("/Users/lizelong/flink-1.9.1/examples/eden.sql"));
////        } catch (IOException e) {
////            e.printStackTrace ();
////        }
////        List<String> nq = sql.stream ().map (p -> {return p+" ";}).collect (Collectors.toList ());
////        Arrays.stream (nq.stream ().collect (Collectors.joining ()).split (";")).filter (p ->{if (p.contains ("--")) {return false;}return true;}).forEach (p ->{
////            try {
////                tEnv.sqlUpdate(p);
////            } catch (SqlParserException e) {
////                throw new RuntimeException("SQL parse failed:\n" + p + "\n", e);
////            }
////        });
//
//        tEnv.sqlUpdate("CREATE TABLE user_log (\n" +
//                "    user_id VARCHAR,\n" +
//                "    item_id VARCHAR,\n" +
//                "    category_id VARCHAR,\n" +
//                "    behavior VARCHAR,\n" +
//                "    ts TIMESTAMP\n" +
//                ") WITH (\n" +
//                "    'connector.type' = 'kafka',\n" +
//                "    'connector.version' = 'universal',\n" +
//                "    'connector.topic' = 'user_behavior',\n" +
//                "    'connector.startup-mode' = 'earliest-offset',\n" +
//                "    'connector.properties.0.key' = 'zookeeper.connect',\n" +
//                "    'connector.properties.0.value' = '10.10.10.49:2181',\n" +
//                "    'connector.properties.1.key' = 'bootstrap.servers',\n" +
//                "    'connector.properties.1.value' = '10.10.10.49:9092',\n" +
//                "    'update-mode' = 'append',\n" +
//                "    'format.type' = 'json',\n" +
//                "    'format.derive-schema' = 'true'\n" +
//                ")");
//
//        tEnv.sqlUpdate("CREATE TABLE pvuv_sink (\n" +
//                "    dt VARCHAR,\n" +
//                "    userId VARCHAR,\n" +
//                "    pv BIGINT,\n" +
//                "    uv BIGINT\n" +
//                ") WITH (\n" +
//                "    'connector.type' = 'jdbc',\n" +
//                "    'connector.url' = 'jdbc:mysql://10.10.10.43:3306/geek_dmp_api',\n" +
//                "    'connector.table' = 'pvuv_sink',\n" +
//                "    'connector.username' = 'root',\n" +
//                "    'connector.password' = 'jieke123',\n" +
//                "    'connector.write.flush.max-rows' = '1'\n" +
//                ")");
//
//        tEnv.sqlUpdate ("INSERT INTO pvuv_sink\n" +
//                "SELECT\n" +
//                "  DATE_FORMAT(ts, 'yyyyMMdd') dt,\n" +
//                "  user_id AS userId,\n" +
//                "  COUNT(*) AS pv,\n" +
//                "  COUNT(DISTINCT user_id) AS uv\n" +
//                "FROM user_log\n" +
//                "GROUP BY DATE_FORMAT(ts, 'yyyyMMdd'),user_id");
//
//        Connection conn = null;
//        PreparedStatement stmt = null;
//        ResultSet rs = null;
//        String rule_sql = "select rule,user_id from pvuv_sink_rule";
//        Map<String,String> ruleMap = new HashMap<> ();
//        try {
//            conn = DriverManager.getConnection("jdbc:mysql://10.10.10.43:3306/geek_dmp_api", "root", "jieke123");
//            stmt = conn.prepareStatement(rule_sql);
//            rs = stmt.executeQuery();
//            while (rs.next()) {
//                ruleMap.put (rs.getString ("user_id"),rs.getString ("rule"));
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
////
////        ruleMap.entrySet ().forEach (p ->{
////            p.getKey ();
////            tEnv.sqlUpdate ("INSERT INTO pvuv_sink\n" +
////                    "SELECT\n" +
////                    "  DATE_FORMAT(ts, 'yyyyMMdd') dt,\n" +
////                    "  user_id AS userId,\n" +
////                    "  COUNT(*) AS pv,\n" +
////                    "  COUNT(DISTINCT user_id) AS uv\n" +
////                    "FROM user_log\n" +
////                    "GROUP BY DATE_FORMAT(ts, 'yyyyMMdd'),user_id");
////        });
//
////        if (rule.equals ("1")) {
////
////        } else {
////            tEnv.sqlUpdate("CREATE TABLE pvuv_sink1 (\n" +
////                    "    dt TIMESTAMP,\n" +
////                    "    userId VARCHAR,\n" +
////                    "    pv BIGINT,\n" +
////                    "    uv BIGINT\n" +
////                    ") WITH (\n" +
////                    "    'connector.type' = 'jdbc',\n" +
////                    "    'connector.url' = 'jdbc:mysql://10.10.10.43:3306/geek_dmp_api',\n" +
////                    "    'connector.table' = 'pvuv_sink1',\n" +
////                    "    'connector.username' = 'root',\n" +
////                    "    'connector.password' = 'jieke123',\n" +
////                    "    'connector.write.flush.max-rows' = '1'\n" +
////                    ")");
////
////            tEnv.sqlUpdate ("INSERT INTO pvuv_sink1\n" +
////                    "SELECT\n" +
////                    "  ts AS dt,\n" +
////                    "  user_id AS userId,\n" +
////                    "  COUNT(*) AS pv,\n" +
////                    "  COUNT(DISTINCT user_id) AS uv\n" +
////                    "FROM user_log\n" +
////                    "GROUP BY ts,user_id");
////        }
//
//
//
//        Table r = tEnv.sqlQuery ("SELECT user_id FROM user_log");
//        DataStream<Event> u = tEnv.toAppendStream (r, Event.class);
//        u.flatMap ((event, collector) -> {
//            // Ruling
//            String rule = ruleMap.get (event.user_id);
//            if (rule != null) {
//                if (examineRule(event.user_id, rule)) {
//                    collector.collect (1);// Rule ID.
//                }
//            }
//        }).print ();
//        try {
//            tEnv.execute("SQL-Job");
//        } catch (Exception e) {
//            e.printStackTrace ();
//        }
//    }
//
//    private boolean examineRule(String user_id, String rule) {
//        return true;
//    }
//
//}
