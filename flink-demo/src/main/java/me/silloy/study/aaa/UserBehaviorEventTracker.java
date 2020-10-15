//package me.silloy.study.aaa;
//
//import io.vertx.core.Vertx;
//import io.vertx.core.VertxOptions;
//import io.vertx.core.json.JsonArray;
//import io.vertx.core.json.JsonObject;
//import io.vertx.ext.jdbc.JDBCClient;
//import io.vertx.ext.sql.SQLClient;
//import io.vertx.ext.sql.SQLConnection;
//import me.silloy.study.aaa.domain.*;
//import org.apache.flink.api.common.functions.MapFunction;
//import org.apache.flink.api.common.serialization.SimpleStringSchema;
//import org.apache.flink.api.common.state.BroadcastState;
//import org.apache.flink.api.common.state.MapStateDescriptor;
//import org.apache.flink.api.common.state.ReadOnlyBroadcastState;
//import org.apache.flink.api.common.typeinfo.BasicTypeInfo;
//import org.apache.flink.api.common.typeinfo.TypeHint;
//import org.apache.flink.api.common.typeinfo.TypeInformation;
//import org.apache.flink.api.java.functions.KeySelector;
//import org.apache.flink.api.java.utils.ParameterTool;
//import org.apache.flink.configuration.Configuration;
//import org.apache.flink.runtime.state.filesystem.FsStateBackend;
//import org.apache.flink.streaming.api.datastream.AsyncDataStream;
//import org.apache.flink.streaming.api.datastream.BroadcastStream;
//import org.apache.flink.streaming.api.datastream.DataStream;
//import org.apache.flink.streaming.api.datastream.KeyedStream;
//import org.apache.flink.streaming.api.environment.CheckpointConfig;
//import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
//import org.apache.flink.streaming.api.functions.async.ResultFuture;
//import org.apache.flink.streaming.api.functions.async.RichAsyncFunction;
//import org.apache.flink.streaming.api.functions.co.KeyedBroadcastProcessFunction;
//import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
//import org.apache.flink.streaming.api.windowing.time.Time;
//import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011;
//import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer011;
//import org.apache.flink.util.Collector;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.util.*;
//import java.util.concurrent.TimeUnit;
//
///**
// * @author: lizelong
// * @date: 2019/11/14
// * @description:
// **/
//public class UserBehaviorEventTracker {
//
//    private static final Logger LOG = LoggerFactory.getLogger(UserBehaviorEventTracker.class);
//    private static final MapStateDescriptor<String, Config> configStateDescriptor = new MapStateDescriptor<>(
//            "configBroadcastState",
//            BasicTypeInfo.STRING_TYPE_INFO,
//            TypeInformation.of(new TypeHint<Config>() {
//            }));
//
//    public static void main(String[] args) throws Exception {
//
//        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
//
//        env.enableCheckpointing(1000);
////        env.setStateBackend(new FsStateBackend("hdfs://10.10.10.49:9000/flink/checkpoints"));
//        CheckpointConfig config = env.getCheckpointConfig();
//        config.enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);
//        config.setCheckpointInterval(60000);
//
//        ParameterTool parameterTool = ParameterTool.fromPropertiesFile(UserBehaviorEventTracker.class
//                .getResourceAsStream("/application.properties"));
//        env.getConfig().setGlobalJobParameters(parameterTool);
//
//        Properties properties = new Properties();
//        properties.setProperty("bootstrap.servers", parameterTool.getRequired("bootstrap.servers"));
//        properties.setProperty("group.id", parameterTool.getRequired("group.id"));
//
//        final KeyedStream<UserEvent, String> customerUserEventStream = env.addSource(new FlinkKafkaConsumer011<>(parameterTool.getRequired("event_behavior_topic"), new SimpleStringSchema(), properties))
//                .map((MapFunction<String, UserEvent>) p -> {
//                    LOG.info("user_behavior msg is : {}", p);
//                    return UserEvent.buildUserEvent(p);
//                }).assignTimestampsAndWatermarks(new UserWatermarkExtractor(Time.hours(24))).keyBy((KeySelector<UserEvent, String>) p -> {
//                    return p.getOne_id();
//                });
//
//        final BroadcastStream<Config> configBroadcastStream = env.addSource(new FlinkKafkaConsumer011<>(parameterTool.getRequired("config_topic"), new SimpleStringSchema(), properties))
//                .map((MapFunction<String, Config>) p -> {
//                    LOG.info("rule_config msg is : {}", p);
//                    return Config.buildConfig(p);
//                }).broadcast(configStateDescriptor);
//
//        final FlinkKafkaProducer011 flinkKafkaProducer = new FlinkKafkaProducer011<>(parameterTool.getRequired("result_topic"), new EvaluatedResultSchema(), properties);
//
//        DataStream<ConnectedResult> connectedStream = customerUserEventStream.connect(configBroadcastStream).process(new ConnectedBroadcastProcessFuntion());
//        DataStream<EvaluatedResult> evaluatedStream = AsyncDataStream.unorderedWait(connectedStream, new AsyncFunction(), 100, TimeUnit.SECONDS, 10);
//        evaluatedStream.addSink(flinkKafkaProducer);
//
//        env.execute("UserBehaviorEventTracker start...");
//    }
//
//
//    private static class AsyncFunction extends RichAsyncFunction<ConnectedResult, EvaluatedResult> {
//
//        private SQLClient client;
//
//        @Override
//        public void open(Configuration parameters) throws Exception {
//
//            ParameterTool parameterTool = (ParameterTool) getRuntimeContext().getExecutionConfig().getGlobalJobParameters();
//
//            Vertx vertx = Vertx.vertx(new VertxOptions ().setWorkerPoolSize(10)
//                    .setEventLoopPoolSize(10));
//
//            String password = parameterTool.getRequired("db_password").equals ("__NO_VALUE_KEY")?"":parameterTool.getRequired("db_password");
//            JsonObject config = new JsonObject ()
//                    .put("url", "jdbc:mysql://"+parameterTool.getRequired("db_ip_host")+"/"+parameterTool.getRequired("db_database")+"?" +
//                            "serverTimezone=GMT%2b8:00&useUnicode=true&characterEncoding=utf8&autoReconnect=true&failOverReadOnly=false&allowMultiQueries=true&useSSL=false")
//                    .put("driver_class", parameterTool.getRequired("driver_class"))
//                    .put("max_pool_size", 10)
//                    .put("user", parameterTool.getRequired("db_user"))
//                    .put("password", password);
//
//            client = JDBCClient.createShared(vertx, config);
//        }
//
//        @Override
//        public void close() throws Exception {
//            client.close();
//        }
//
//        @Override
//        public void asyncInvoke(ConnectedResult connectedResult, ResultFuture<EvaluatedResult> resultFuture) throws Exception {
//            incremental(connectedResult, resultFuture);
//        }
//
//        private void incremental(ConnectedResult connectedResult, ResultFuture<EvaluatedResult> resultFuture) {
//
//            UserEvent userEvent = connectedResult.getUserEvent();
//            Map<String, Config> configState = connectedResult.getConfigState();
//            ArrayList<EvaluatedResult> l = new ArrayList<EvaluatedResult>();
//            client.getConnection(conn -> {
//                if (conn.failed()) {
//                    conn.cause().printStackTrace();
//                    return;
//                }
//
//                String event = userEvent.getEvent();
//                final SQLConnection connection = conn.result();
//                try {
//                    if (event.equals("purchase_success")) {
//                        String sql = "INSERT INTO purchase_success VALUES (?, ?, ?, ?, ?, ?)";
//                        LOG.info("insert into sql is : {}", sql);
//                        LOG.info("userEvent is : {}", userEvent);
//                        connection.updateWithParams(sql, new JsonArray().add(userEvent.getOne_id()).add(userEvent.getEvent_timestamp())
//                                .add(userEvent.getEvent_time()).add(userEvent.getEvent_channel()).add(userEvent.getType()).add(userEvent.getEvent()), res -> {
//                            if (res.failed()) {
//                                LOG.error("insert into purchase_success error is : ", res.cause());
//                            } else {
//                                try {
//
//                                    Iterator<Map.Entry<String, Config>> iter = configState.entrySet().iterator();
//                                    while (iter.hasNext()) {
//                                        Map.Entry<String, Config> p = iter.next();
//                                        LOG.info("rule_config is : {}", p);
//                                        Node node = Node.buildNode(p.getValue().getNode());
//                                        if (node.calculate()) {
//                                            EvaluatedResult evaluatedResult = new EvaluatedResult();
//                                            evaluatedResult.setEvent_id(p.getKey());
//                                            evaluatedResult.setOne_id(userEvent.getOne_id());
//                                            l.add(evaluatedResult);
//                                            LOG.info("evaluatedResult is : {}", evaluatedResult);
//                                        }
//                                    }
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                            resultFuture.complete(l);
//                            connection.close();
//                        });
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    connection.close();
//                }
//            });
//        }
//
//        @Override
//        public void timeout(ConnectedResult input, ResultFuture<EvaluatedResult> resultFuture) throws Exception {
//
//        }
//    }
//
//    private static class ConnectedBroadcastProcessFuntion extends
//            KeyedBroadcastProcessFunction<String, UserEvent, Config, ConnectedResult> {
//
//        public ConnectedBroadcastProcessFuntion() {
//            super();
//        }
//
//        @Override
//        public void processElement(UserEvent userEvent, ReadOnlyContext ctx, Collector<ConnectedResult> collector)
//                throws Exception {
//
//            ReadOnlyBroadcastState<String, Config> configState = ctx.getBroadcastState(configStateDescriptor);
//            Map<String, Config> map = new HashMap<>();
//            configState.immutableEntries().forEach(p -> {
//                map.put(p.getKey(), p.getValue());
//            });
//
//            ConnectedResult r = ConnectedResult.buildConnectedResult(userEvent, map);
//            collector.collect(Optional.of(r).get());
//        }
//
//        @Override
//        public void processBroadcastElement(Config config, Context context, Collector<ConnectedResult> collector)
//                throws Exception {
//
//            String event_id = config.getEvent_id();
//            boolean remove = config.isRemove();
//            BroadcastState<String, Config> state = context.getBroadcastState(configStateDescriptor);
//
//            if (remove) {
//                state.remove(event_id);
//            } else {
//                state.put(event_id, config);
//            }
//        }
//    }
//
//    private static class UserWatermarkExtractor extends
//            BoundedOutOfOrdernessTimestampExtractor<UserEvent> {
//
//        public UserWatermarkExtractor(Time maxOutOfOrderness) {
//            super(maxOutOfOrderness);
//        }
//
//        @Override
//        public long extractTimestamp(UserEvent element) {
//            return element.getEvent_timestamp();
//        }
//
//    }
//
//}