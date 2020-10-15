package me.silloy.study.job;

import com.google.common.collect.Lists;
import lombok.Data;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.apache.flink.test.util.AbstractTestBase;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author shaohuasu
 * @date 2019/12/11 2:17 PM
 * @since 1.8
 */
public class SocketWindowWordCountTest extends AbstractTestBase {

    private final static Logger LOGGER = LoggerFactory.getLogger(SocketWindowWordCountTest.class);

    @Test
    public void main() throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        env.setParallelism(1);

        CollectSink.values.clear();

        env.fromElements(1L, 21L, 22L)
                .map(new MultiplyByTwo())
                .addSink(new CollectSink());

        env.execute();
        assertEquals(Lists.newArrayList(2L, 42L, 44L), CollectSink.values);
    }


    private static class CollectSink implements SinkFunction<Long> {
        // must be static
        public static final List<Long> values = new ArrayList<>();

        @Override
        public synchronized void invoke(Long value, Context context) throws Exception {
            LOGGER.info("------ context.processingtime: {}", context.currentProcessingTime());
            LOGGER.info("------ context.waterwark: {}", context.currentWatermark());
            LOGGER.info("------ context.timestamp: {}", context.timestamp());
            values.add(value);
        }
    }

    @Data
    private static class MultiplyByTwo implements MapFunction<Long, Long> {

        @Override
        public Long map(Long v) throws Exception {
            return v * 2;
        }
    }
}