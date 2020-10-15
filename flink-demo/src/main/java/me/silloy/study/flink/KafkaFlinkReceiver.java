package me.silloy.study.flink;

import com.alibaba.fastjson.JSON;
import me.silloy.study.bean.Student;
import me.silloy.study.operator.CountWindowAverage;
import me.silloy.study.sink.SinkToMySQL;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011;
import org.apache.flink.util.Collector;

import java.util.Properties;

/**
 * @author shaohuasu
 * @date 2019/11/26 11:36 AM
 * @since 1.8
 */
public class KafkaFlinkReceiver {

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

        DataStreamSource<String> dataStreamSource = env.addSource(new FlinkKafkaConsumer011<>(
                "metric_local",
                new SimpleStringSchema(),
                props
        )).setParallelism(1)
                ;

        SingleOutputStreamOperator<Student> student = env.addSource(new FlinkKafkaConsumer011<>(
                "metric_local",
                new SimpleStringSchema(),
                props)).setParallelism(1)
                .map(string -> JSON.parseObject(string, Student.class));

        SingleOutputStreamOperator<Student> fm = student.flatMap(new FlatMapFunction<Student, Student>() {
            @Override
            public void flatMap(Student student, Collector<Student> collector) throws Exception {
                if (student.getId() % 2 == 0) {
                    collector.collect(student);
                }
            }
        })
                .filter(new FilterFunction<Student>() {
                    @Override
                    public boolean filter(Student student) throws Exception {
                        if (student.getId() > 95) {
                            return true;
                        }
                        return false;
                    }
                })

                .keyBy(new KeySelector<Student, Integer>() {

                    @Override
                    public Integer getKey(Student student) throws Exception {
                        return student.getAge();
                    }
                })



                .reduce(new ReduceFunction<Student>() {
                    @Override
                    public Student reduce(Student value1, Student value2) throws Exception {
                        Student student1 = new Student();
                        student1.name = value1.name + value2.name;
                        student1.id = (value1.id + value2.id) / 2;
                        student1.password = value1.password + value2.password;
                        student1.age = (value1.age + value2.age) / 2;
                        return student1;
                    }
                })
               ;
        fm.print();

        student.addSink(new SinkToMySQL());

        dataStreamSource.print();

        env.execute("Flink add data source");
    }



//    public static void main2(String[] args) throws Exception{
//        final ParameterTool parameterTool = ExecutionEnvUtil.createParameterTool(args);
//        StreamExecutionEnvironment env = ExecutionEnvUtil.prepare(parameterTool);
//        DataStreamSource<Metrics> data = KafkaConfigUtil.buildSource(env);
//
//        data.addSink(new FlinkKafkaProducer011<Metrics>(
//                parameterTool.get("kafka.sink.brokers"),
//                parameterTool.get("kafka.sink.topic"),
//                new MetricSchema()
//        )).name("flink-connectors-kafka")
//                .setParallelism(parameterTool.getInt("stream.sink.parallelism"));
//
//        env.execute("flink learning connectors kafka");
//    }
}
