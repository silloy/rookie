package me.silloy.study.aaa.domain;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Map;


@Data
public class ConnectedResult implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectedResult.class);

    private UserEvent userEvent;
    private Map<String, RuleConfig> configState;

    public static ConnectedResult buildConnectedResult(UserEvent userEvent,
                                                       Map<String, RuleConfig> configState) {

        ConnectedResult connectedResult = new ConnectedResult();
        connectedResult.setConfigState(configState);
        connectedResult.setUserEvent(userEvent);

        LOGGER.info("connectedResult: " + JSON.toJSONString(connectedResult));

        return connectedResult;
    }
}
