package me.silloy.netty.chat.protocol.packet.response;

import me.silloy.netty.chat.protocol.packet.Packet;
import me.silloy.netty.chat.session.Session;
import lombok.Data;

import java.util.List;

import static me.silloy.netty.chat.protocol.Command.LIST_GROUP_MEMBER_RESPONSE;

/**
 * @author shaohuasu
 * @date 2019-01-03 20:31
 * @since 1.8
 */
@Data
public class ListGroupMembersResponsePacket extends Packet {

    private String groupId;

    private List<Session> sessionList;

    @Override
    public Byte getCommand() {
        return LIST_GROUP_MEMBER_RESPONSE;
    }
}
