package me.silloy.study.websocket.netty.config;

import lombok.Data;

@Data
public class Event {
    private String eventId;
    private String eventDt;

    public Event(String eventId, String eventDt) {
        this.eventId = eventId;
        this.eventDt = eventDt;
    }

}
