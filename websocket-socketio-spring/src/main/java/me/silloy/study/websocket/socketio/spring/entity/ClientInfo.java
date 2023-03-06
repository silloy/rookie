package me.silloy.study.websocket.socketio.spring.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author shaohuasu
 * @since 1.8
 */
@Data
public class ClientInfo {

    private String clientid;
    private Short connected;
    private Long mostsignbits;
    private Long leastsignbits;
    private Date lastconnecteddate;

}
