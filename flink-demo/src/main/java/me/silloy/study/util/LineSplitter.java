package me.silloy.study.util;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;

/**
 * @author shaohuasu
 * @date 2019/12/11 2:45 PM
 * @since 1.8
 */
public final class LineSplitter implements FlatMapFunction<String, Tuple2<String, Integer>> {
    @Override
    public void flatMap(String s, Collector<Tuple2<String, Integer>> collector) {
        String[] tokens = s.toLowerCase().split("\\W+");

        for (String token : tokens) {
            if (token.length() > 0) {
                collector.collect(new Tuple2<String, Integer>(token, 1));
            }
        }
    }
}
