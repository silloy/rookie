package me.silloy.study.flink.touch;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.DoubleNode;
import me.silloy.study.aaa.domain.*;
import me.silloy.study.operator.EventBroadcastProcessFunction;
import me.silloy.study.operator.EventProcess;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringEncoder;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.core.io.SimpleVersionedSerializer;
import org.apache.flink.streaming.api.datastream.AsyncDataStream;
import org.apache.flink.streaming.api.datastream.BroadcastStream;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.filesystem.BucketAssigner;
import org.apache.flink.streaming.api.functions.sink.filesystem.StreamingFileSink;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer011;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * @author shaohuasu
 * @date 2019/12/17 2:05 PM
 * @since 1.8
 */
public class EventProcessTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventProcessTask.class);

    public static void main(String[] args) throws Exception {

        ParameterTool parameterTool = ParameterTool.fromPropertiesFile(EventProcessTask.class
                .getResourceAsStream("/application.properties"));

        String eventTopic = parameterTool.getRequired("event_behavior_topic");
        String resultTopic = parameterTool.getRequired("result_topic");
        String configTopic = parameterTool.getRequired("config_topic");
        String servers = parameterTool.getRequired("bootstrap.servers");
        String groupId = parameterTool.getRequired("group.id");


        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        env.enableCheckpointing(1000);
//        env.setStateBackend(new FsStateBackend("hdfs://10.10.10.49:9000/flink/checkpoints"));
        CheckpointConfig config = env.getCheckpointConfig();
        config.enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);
        config.setCheckpointInterval(60000);

        env.getConfig().setGlobalJobParameters(parameterTool);

        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", servers);
        properties.setProperty("group.id", groupId);


        final KeyedStream<UserEvent, String> eventStream = env
                .addSource(new FlinkKafkaConsumer011<>(eventTopic, new SimpleStringSchema(), properties))
                .map((MapFunction<String, UserEvent>) p -> {
                    LOGGER.info("user_behavior msg is : {}", p);
                    return UserEvent.buildUserEvent(p);
                }).assignTimestampsAndWatermarks(new EventProcessTask.UserWatermarkExtractor(Time.hours(24)))
                .keyBy((KeySelector<UserEvent, String>) p -> {
                    return p.getOneId();
                });



        final BroadcastStream<RuleConfig> ruleStream = env.addSource(new FlinkKafkaConsumer011<>(configTopic, new SimpleStringSchema(), properties))
                .map((MapFunction<String, RuleConfig>) p -> {
                    LOGGER.info("rule_config msg is : {}", p);
                    return RuleConfig.buildConfig(p);
                }).broadcast(EventBroadcastProcessFunction.RULE_CONFIG);


        DataStream<ConnectedResult> connectedStream = eventStream.connect(ruleStream)
                .process(new EventBroadcastProcessFunction());

        DataStream<EvaluatedResult> evaluatedStream = AsyncDataStream
                .unorderedWait(connectedStream, new EventProcess(), 100, TimeUnit.SECONDS, 10);

        final FlinkKafkaProducer011 finalSink = new FlinkKafkaProducer011<>(
                resultTopic,
                new EvaluatedResultSchema(),
                properties);
        evaluatedStream.addSink(finalSink);

        env.execute("UserBehaviorEventTracker start...");


//        StreamingFileSink<String> sink = StreamingFileSink
//                .forRowFormat(new Path("/tmp/kafka-loader"), new SimpleStringEncoder<String>())
//                .withBucketAssigner(new EventTimeBucketAssigner())
//                .build();
//        evaluatedStream.addSink(sink);
    }


    private static class UserWatermarkExtractor extends
            BoundedOutOfOrdernessTimestampExtractor<UserEvent> {

        public UserWatermarkExtractor(Time maxOutOfOrderness) {
            super(maxOutOfOrderness);
        }

        @Override
        public long extractTimestamp(UserEvent element) {
            return element.getEventTimestamp();
        }

    }





    public class EventTimeBucketAssigner implements BucketAssigner<String, String> {
        @Override
        public String getBucketId(String element, Context context) {
//            JsonNode node = mapper.readTree(element);
            JsonNode node = new DoubleNode(12);
            long date = (long) (node.path("timestamp").floatValue() * 1000);
            String partitionValue = new SimpleDateFormat("yyyyMMdd").format(new Date(date));
            return "dt=" + partitionValue;
        }

        @Override
        public SimpleVersionedSerializer<String> getSerializer() {
            return null;
        }
    }

}
