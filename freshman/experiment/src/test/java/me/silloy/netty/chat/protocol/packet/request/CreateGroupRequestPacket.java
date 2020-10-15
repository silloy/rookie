package me.silloy.netty.chat.protocol.packet.request;

import me.silloy.netty.chat.protocol.packet.Packet;
import lombok.Data;

import java.util.List;

import static me.silloy.netty.chat.protocol.Command.GROUP_REQUEST;

/**
 * @author shaohuasu
 * @date 2019-01-03 15:37
 * @since 1.8
 */
@Data
public class CreateGroupRequestPacket extends Packet {

    private List<String> userIdList;

    @Override
    public Byte getCommand() {
        return GROUP_REQUEST;
    }
}
