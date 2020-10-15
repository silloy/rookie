package me.silloy.netty.chat.protocol.packet.request;

import me.silloy.netty.chat.protocol.packet.Packet;
import lombok.Data;

import static me.silloy.netty.chat.protocol.Command.MESSAGE_REQUEST;

/**
 * @author shaohuasu
 * @date 2018-12-27 13:30
 * @since 1.8
 */
@Data
public class MessageRequestPacket extends Packet {

    private String toUserId;

    private String message;


    @Override
    public Byte getCommand() {
        return MESSAGE_REQUEST;
    }

    public MessageRequestPacket() {
    }

    public MessageRequestPacket(String message) {
        this.message = message;
    }

    public MessageRequestPacket(String touserId, String message) {
        this.toUserId = touserId;
        this.message = message;
    }

}
