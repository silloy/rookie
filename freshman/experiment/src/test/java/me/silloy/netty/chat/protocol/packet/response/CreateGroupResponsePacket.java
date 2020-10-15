package me.silloy.netty.chat.protocol.packet.response;

import me.silloy.netty.chat.protocol.packet.Packet;
import lombok.Data;

import java.util.List;

import static me.silloy.netty.chat.protocol.Command.GROUP_RESPONSE;

/**
 * @author shaohuasu
 * @date 2019-01-03 15:48
 * @since 1.8
 */
@Data
public class CreateGroupResponsePacket extends Packet {
    private Boolean success;

    private String groupId;

    private List<String> userNameList;

    @Override
    public Byte getCommand() {
        return GROUP_RESPONSE;
    }
}
