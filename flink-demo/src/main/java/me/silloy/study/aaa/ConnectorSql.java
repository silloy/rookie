//package me.silloy.study.aaa;
//
//import me.silloy.study.aaa.domain.User;
//import org.apache.flink.api.common.functions.FlatMapFunction;
//import org.apache.flink.api.common.typeinfo.TypeInformation;
//import org.apache.flink.api.common.typeinfo.Types;
//import org.apache.flink.api.java.tuple.Tuple2;
//import org.apache.flink.formats.json.JsonRowSerializationSchema;
//import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
//import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
//import org.apache.flink.table.api.EnvironmentSettings;
//import org.apache.flink.table.api.Table;
//import org.apache.flink.table.api.TableSchema;
//import org.apache.flink.table.api.java.StreamTableEnvironment;
//import org.apache.flink.table.descriptors.Json;
//import org.apache.flink.table.descriptors.Kafka;
//import org.apache.flink.table.descriptors.Schema;
//import org.apache.flink.types.Row;
//import org.apache.flink.util.Collector;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.util.Properties;
//
//
///**
// * @author: lizelong
// * @date: 2019/11/1
// * @description:
// **/
//public class ConnectorSql {
//
//    private static final Logger log = LoggerFactory.getLogger(ConnectorSql.class);
//
//    public static void main(String[] args) throws Exception {
//        ConnectorSql connectorSql = new ConnectorSql ();
//        connectorSql.connector ();
//    }
//
//    private StreamTableEnvironment tEnv;
//
//    private void connector() {
//
//        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
//        EnvironmentSettings settings = EnvironmentSettings.newInstance()
//                .useBlinkPlanner()
//                .inStreamingMode()
//                .build();
//        this.tEnv = StreamTableEnvironment.create(env,settings);
//        tEnv.connect (new Kafka ()
//                .version ("universal")
//                .topic ("user_behavior")
//                .property ("bootstrap.servers","10.10.10.49:9092")
//                .property ("zookeeper.connect","10.10.10.49:2181")
//                .property ("group.id","test")
//                .startFromLatest()
//        )
//                .withFormat(new Json().failOnMissingField(false).deriveSchema())
//                .withSchema(new Schema ()
////                        .rowtime(new Rowtime ().timestampsFromSource().watermarksPeriodicBounded(2000))
//                        .field("user_id", Types.STRING)
//                        .field("item_id", Types.STRING)
//                        .field("category_id", Types.STRING)
//                        .field("behavior", Types.STRING)
//                        .field("ts", Types.STRING)
//                )
//                .inAppendMode ()
//                .registerTableSource("user_log");
//
////        tEnv.connect(new Kafka().version("universal")
////                        .topic("connector_sink")
////                        .property("acks", "all")
////                        .property("retries", "0")
////                        .property("batch.size", "16384")
////                        .property("linger.ms", "10")
////                        .property("bootstrap.servers", "10.10.10.49:9092")
////                        .sinkPartitionerFixed()
////        ).inAppendMode().withFormat(new Json().deriveSchema())
////                .withSchema(new Schema()
////                        .field("dt", Types.STRING)
////                        .field("userId", Types.STRING)
////                        .field("pv", Types.LONG)
////                        .field("uv", Types.LONG)
////                )
////                .registerTableSink("pvuv_sink");
//
//
////        tEnv.sqlUpdate ("INSERT INTO pvuv_sink\n" +
////                "SELECT\n" +
////                "  DATE_FORMAT(ts, 'yyyyMMdd') dt,\n" +
////                "  user_id AS userId,\n" +
////                "  COUNT(*) AS pv,\n" +
////                "  COUNT(DISTINCT user_id) AS uv\n" +
////                "FROM user_log\n" +
////                "GROUP BY DATE_FORMAT(ts, 'yyyyMMdd'),user_id");
//
//        TableSchema tableSchema = new TableSchema(new String[]{"dt","userId","pv","uv"}, new TypeInformation[]{Types.STRING,Types.STRING,Types.LONG,Types.LONG});
//        TypeInformation<Row> typeInfo = tableSchema.toRowType();
////
//        Properties properties = new Properties ();
//        properties.setProperty ("acks", "all");
//        properties.setProperty ("retries", "0");
//        properties.setProperty ("batch.size", "16384");
//        properties.setProperty ("linger.ms", "10");
//        properties.setProperty ("bootstrap.servers", "10.10.10.49:9092");
////
//        JsonRowSerializationSchema seria = new JsonRowSerializationSchema.Builder (typeInfo).build ();
////
////        KafkaTableSink sink = new KafkaTableSink(
////                tableSchema,
////                "connector_sink",
////                properties,
////                Optional.of(new FlinkFixedPartitioner<>()),
////                seria);
////
////
//////        KafkaTableSink tableSink = new KafkaTableSink (tableSchema, "connector_sink", properties,
//////                Optional.of (new FlinkFixedPartitioner ()),
//////                new JsonRowSerializationSchema.Builder (""));
////
////        tEnv.registerTableSink ("pvuv_sink",sink);
////
////        tEnv.toRetractStream (r,User.class).addSink(new FlinkKafkaProducer<Tuple2<Boolean, User>>("connector_sink", new ObjSerializationSchema("connector_sink"),
////                properties, FlinkKafkaProducer.Semantic.EXACTLY_ONCE));
////        tEnv.toRetractStream (r,User.class).addSink (new FlinkKafkaProducer010<Tuple2<Boolean, User>> ("10.10.10.49:9092","connector_sink",seria ));
//
//        Table r = tEnv.sqlQuery ("SELECT\n" +
//                "  ts AS dt,\n" +
//                "  user_id AS userId,\n" +
////                "  user_id AS userId\n" +
////                "  COUNT(*) AS pv,\n" +
//                "  CAST(COUNT(DISTINCT user_id) AS CHAR) AS uv\n" +
//                "FROM user_log\n" +
////                "FROM user_log\n");
//                "GROUP BY ts,user_id");
////                "GROUP BY user_id");
////        r.insertInto("pvuv_sink");
//
//        boolean[] flag = new boolean[1];
//
////        tEnv.toRetractStream (r,User.class).flatMap (new FlatMapFunction<Tuple2<Boolean, User>, Tuple2<Boolean, User>> () {
////            @Override
////            public void flatMap(Tuple2<Boolean, User> value, Collector<Tuple2<Boolean, User>> out) throws Exception {
////                if (Math.random () < 0.5) {
////                    out.collect (value);
////                } else {
////                    User u = new User ();
////                    u.setUserId ("Rule failed");
////                    out.collect (new Tuple2<> (true, u));
////                }
////            }
////        }).addSink(new FlinkKafkaProducer<Tuple2<Boolean, User>>("10.10.10.49:9092", "connector_sink", new ObjSerializationSchema()));
//
//        tEnv.toRetractStream (r, User.class).flatMap (new FlatMapFunction<Tuple2<Boolean, User>, User> () {
//
//            @Override
//            public void flatMap(Tuple2<Boolean, User> booleanUserTuple2, Collector<User> collector) throws Exception {
//
//            }
//        });
//
//
//        SingleOutputStreamOperator<Tuple2<Boolean, User>> streamt = tEnv.toRetractStream (r, User.class).flatMap (new FlatMapFunction<Tuple2<Boolean, User>, Tuple2<Boolean, User>> () {
//            @Override
//            public void flatMap(Tuple2<Boolean, User> value, Collector<Tuple2<Boolean, User>> out) throws Exception {
//                if (Math.random () < 0.5) {
//                    out.collect (value);
//                } else {
//                    User u = new User ();
//                    u.setUserId ("Rule failed");
//                    out.collect (new Tuple2<> (true, u));
//                }
//            }
//        });
//        try {
//            tEnv.execute("ConnectorSql-Job");
//        } catch (Exception e) {
//            e.printStackTrace ();
//        }
////        new Thread (() -> {
////            System.out.println ("Starts waiting");
////            try {
////                Thread.sleep (15000L);
////            } catch (Exception e) {}
////            System.out.println ("Ends waiting");
////            flag[0] = true;
////        }).start ();
//    }
//
//}
