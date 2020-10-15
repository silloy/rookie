package me.silloy.study.flink;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * @author shaohuasu
 * @date 2019/11/26 11:24 AM
 * @since 1.8
 */
public class SocketTextStreamWordCountTest {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private String topic = "dev-member-registe";

    @Test
    public void consumer() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "10.10.10.49:9092");
        props.put("group.id", "test6");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        @SuppressWarnings("resource")
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList(topic));
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofHours(8));
            for (ConsumerRecord<String, String> record : records)
                logger.info("timestamp = {} , timestampType = {}, offset = {}, key = {}, value = {}",
                        record.timestamp(), record.timestampType(), record.offset(), record.key(), record.value());
//                System.out.printf("offset = %d, key = %s, value = %s", record.offset(), record.key(), record.value());
        }
    }


    @Test
    public void producer() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "10.10.10.49:9092");
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        Producer<String, String> producer = new KafkaProducer<>(props);
        for (int i = 0; i < 100; i++) {
//            producer.sendOffsetsToTransaction();
            producer.send(new ProducerRecord<String, String>(topic,
                    Integer.toString(i),
                    Integer.toString(i)));
        }
        producer.close();
    }

    @Test
    public void list() {
        List<Integer> in = Lists.newArrayList(12,12,23,234,34,23,2343,5,4,323,2,2124,23,23);
        Lists.partition(in, 5).forEach(l -> {
            System.out.println(JSON.toJSONString(l));
        });
    }
}