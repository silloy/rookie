package me.silloy.study.job;

import me.silloy.study.util.LineSplitter;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;

/**
 * @author shaohuasu
 * @date 2019/12/11 2:38 PM
 * @since 1.8
 */
public class SimpleWordCountTask {
    public static void main(String[] args) throws Exception {
        final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        DataSet<String> text = env.fromElements(
                "Who's there?",
                "I think I hear them. Stand, ho! Who's there?");

        DataSet<Tuple2<String, Integer>> wcs = text
                .flatMap(new LineSplitter())
                .groupBy(0)
                .sum(1);

        wcs.print();
    }

}
