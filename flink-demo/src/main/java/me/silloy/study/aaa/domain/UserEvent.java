package me.silloy.study.aaa.domain;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * @author: lizelong
 * @date: 2019/11/26
 * @description:
 **/
@Data
public class UserEvent implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger (UserEvent.class);

    private String oneId;
    private Long eventTimestamp;
    private String eventTime;
    private String eventChannel;
    private String type;
    private String event;
    private JSONObject data;

    public static UserEvent buildUserEvent(String eventStr) {

        JSONObject json = JSONObject.parseObject (eventStr);
        UserEvent userEvent = new UserEvent();

        userEvent.setOneId(json.getString ("one_id"));
        userEvent.setEventTimestamp(json.getLong ("event_timestamp"));
        userEvent.setEventTime(json.getString ("event_time"));
        userEvent.setEventChannel(json.getString ("event_channel"));
        userEvent.setType(json.getString ("type"));
        userEvent.setEvent(json.getString ("event"));
        userEvent.setData( json.getJSONObject ("data"));

        return userEvent;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
