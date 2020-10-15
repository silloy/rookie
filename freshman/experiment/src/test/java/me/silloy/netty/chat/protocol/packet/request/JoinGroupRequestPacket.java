package me.silloy.netty.chat.protocol.packet.request;

import me.silloy.netty.chat.protocol.packet.Packet;
import lombok.Data;

import static me.silloy.netty.chat.protocol.Command.JOIN_GROUP_REQUEST;

/**
 * @author shaohuasu
 * @date 2019-01-03 20:01
 * @since 1.8
 */
@Data
public class JoinGroupRequestPacket extends Packet {

    private String groupId;

    @Override
    public Byte getCommand() {
        return JOIN_GROUP_REQUEST;
    }
}
