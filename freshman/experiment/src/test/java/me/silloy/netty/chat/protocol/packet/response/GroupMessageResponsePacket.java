package me.silloy.netty.chat.protocol.packet.response;

import me.silloy.netty.chat.protocol.packet.Packet;
import me.silloy.netty.chat.session.Session;
import lombok.Data;

import static me.silloy.netty.chat.protocol.Command.GROUP_MESSAGE_RESPONSE;

/**
 * @author shaohuasu
 * @date 2019-01-04 10:29
 * @since 1.8
 */
@Data
public class GroupMessageResponsePacket extends Packet {

    private String fromGroupId;

    private String message;

    private Session fromUser;

    @Override
    public Byte getCommand() {
        return GROUP_MESSAGE_RESPONSE;
    }
}
