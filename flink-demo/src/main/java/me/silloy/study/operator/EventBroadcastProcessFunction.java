package me.silloy.study.operator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.DoubleNode;
import me.silloy.study.aaa.domain.RuleConfig;
import me.silloy.study.aaa.domain.ConnectedResult;
import me.silloy.study.aaa.domain.UserEvent;
import org.apache.flink.api.common.state.BroadcastState;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.common.state.ReadOnlyBroadcastState;
import org.apache.flink.api.common.typeinfo.BasicTypeInfo;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.core.io.SimpleVersionedSerializer;
import org.apache.flink.streaming.api.functions.co.KeyedBroadcastProcessFunction;
import org.apache.flink.streaming.api.functions.sink.filesystem.BucketAssigner;
import org.apache.flink.util.Collector;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author shaohuasu
 * @date 2019/12/17 3:17 PM
 * @since 1.8
 */
public class EventBroadcastProcessFunction extends
        KeyedBroadcastProcessFunction<String, UserEvent, RuleConfig, ConnectedResult> {

    public static final MapStateDescriptor<String, RuleConfig> RULE_CONFIG = new MapStateDescriptor<>(
            "rule_config",
            BasicTypeInfo.STRING_TYPE_INFO,
            TypeInformation.of(new TypeHint<RuleConfig>() {
            }));

    public EventBroadcastProcessFunction() {
        super();
    }

    @Override
    public void processElement(UserEvent userEvent, ReadOnlyContext ctx, Collector<ConnectedResult> collector)
            throws Exception {

        ReadOnlyBroadcastState<String, RuleConfig> configState = ctx.getBroadcastState(RULE_CONFIG);
        Map<String, RuleConfig> map = new HashMap<>();
        configState.immutableEntries().forEach(p -> {
            map.put(p.getKey(), p.getValue());
        });
        ConnectedResult r = ConnectedResult.buildConnectedResult(userEvent, map);
        collector.collect(Optional.of(r).get());
    }



    @Override
    public void processBroadcastElement(RuleConfig config, Context context, Collector<ConnectedResult> collector)
            throws Exception {
        Long planid = config.getPlanid();
        BroadcastState<String, RuleConfig> state = context.getBroadcastState(RULE_CONFIG);
        state.put(planid.toString(), config);
    }
}