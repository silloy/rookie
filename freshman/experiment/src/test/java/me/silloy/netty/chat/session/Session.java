package me.silloy.netty.chat.session;

import lombok.Data;

/**
 * @author shaohuasu
 * @date 2019-01-03 13:30
 * @since 1.8
 */
@Data
public class Session {

    private String userId;

    private String username;

    public Session() {
    }

    public Session(String userId, String username) {
        this.userId = userId;
        this.username = username;
    }
}
