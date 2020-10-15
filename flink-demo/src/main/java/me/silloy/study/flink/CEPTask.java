package me.silloy.study.flink;

import com.alibaba.fastjson.JSON;
import me.silloy.study.bean.WebEvent;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.cep.CEP;
import org.apache.flink.cep.PatternSelectFunction;
import org.apache.flink.cep.PatternStream;
import org.apache.flink.cep.functions.PatternProcessFunction;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.cep.pattern.conditions.SimpleCondition;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011;
import org.apache.flink.util.Collector;

import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author shaohuasu
 * @date 2019/12/11 7:33 PM
 * @since 1.8
 */
public class CEPTask {
    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        Properties props = new Properties();
        props.put("bootstrap.servers", "10.10.10.49:9092");
        props.put("group.id", "test6");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("auto.offset.reset", "latest");

//        DataStreamSource
        SingleOutputStreamOperator<WebEvent> es = env.addSource(new FlinkKafkaConsumer011<>(
                "user_behavior_test",
                new SimpleStringSchema(),
                props
        )).setParallelism(1)
                .map(new MapFunction<String, WebEvent>() {
                    @Override
                    public WebEvent map(String value) throws Exception {
                        return JSON.parseObject(value, WebEvent.class);
                    }
                });

        Pattern<WebEvent, ?> pattern = Pattern.<WebEvent>begin("start").where(
                new SimpleCondition<WebEvent>() {
                    @Override
                    public boolean filter(WebEvent value) throws Exception {
                        return true;
                    }
                }
        )
                .next("end")
                .within(Time.seconds(20))
                ;

        PatternStream<WebEvent> ps = CEP.pattern(es, pattern);

        DataStream<String> result = ps.process(new PatternProcessFunction<WebEvent, String>() {
            @Override
            public void processMatch(Map<String, List<WebEvent>> pattern,
                                     Context context,
                                     Collector<String> collector) throws Exception {
                collector.collect("abc");
//                collector.collect(createResultFrom(pattern));
            }
        });

        ps.select(new PatternSelectFunction<WebEvent, Object>() {
            @Override
            public Object select(Map<String, List<WebEvent>> map) throws Exception {
                return null;
            }
        });

        es.print();
        env.execute();
    }
}
