package me.silloy.netty.chat.protocol.packet.request;

import me.silloy.netty.chat.protocol.packet.Packet;
import lombok.Data;

import static me.silloy.netty.chat.protocol.Command.LIST_GROUP_MEMBER_REQUEST;

/**
 * @author shaohuasu
 * @date 2019-01-03 20:29
 * @since 1.8
 */
@Data
public class ListGroupMembersRequestPacket extends Packet {

    private String groupId;

    @Override
    public Byte getCommand() {
        return LIST_GROUP_MEMBER_REQUEST;
    }
}
