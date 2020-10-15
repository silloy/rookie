package me.silloy.netty.chat.protocol.packet.response;

import me.silloy.netty.chat.protocol.packet.Packet;
import lombok.Data;

import static me.silloy.netty.chat.protocol.Command.JOIN_GROUP_RESPONSE;

/**
 * @author shaohuasu
 * @date 2019-01-03 20:07
 * @since 1.8
 */
@Data
public class JoinGroupResponsePacket extends Packet {

    private Boolean success;

    private String groupId;

    private String reason;

    @Override
    public Byte getCommand() {
        return JOIN_GROUP_RESPONSE;
    }

    public boolean isSuccess() {
        return success;
    }
}
