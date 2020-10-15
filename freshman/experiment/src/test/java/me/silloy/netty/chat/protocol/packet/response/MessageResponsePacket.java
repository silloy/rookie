package me.silloy.netty.chat.protocol.packet.response;

import me.silloy.netty.chat.protocol.packet.Packet;
import lombok.Data;

import static me.silloy.netty.chat.protocol.Command.MESSAGE_RESPONSE;

/**
 * @author shaohuasu
 * @date 2018-12-27 13:30
 * @since 1.8
 */
@Data
public class MessageResponsePacket extends Packet {

    private String fromUserId;

    private String fromUserName;

    private String message;

    @Override
    public Byte getCommand() {
        return MESSAGE_RESPONSE;
    }
}
