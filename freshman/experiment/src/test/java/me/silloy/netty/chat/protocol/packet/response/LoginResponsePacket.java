package me.silloy.netty.chat.protocol.packet.response;

import me.silloy.netty.chat.protocol.packet.Packet;
import lombok.Data;

import static me.silloy.netty.chat.protocol.Command.LOGIN_RESPONSE;

/**
 * @author shaohuasu
 * @date 2018-12-26 21:59
 * @since 1.8
 */
@Data
public class LoginResponsePacket extends Packet {

    private String userId;

    private String username;

    private String reason;

    private Boolean success;

    @Override
    public Byte getCommand() {
        return LOGIN_RESPONSE;
    }

    public boolean isSuccess() {
        return success;
    }
}
