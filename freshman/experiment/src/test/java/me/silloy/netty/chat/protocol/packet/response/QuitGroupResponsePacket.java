package me.silloy.netty.chat.protocol.packet.response;

import me.silloy.netty.chat.protocol.packet.Packet;
import lombok.Data;

import static me.silloy.netty.chat.protocol.Command.QUIT_GROUP_RESPONSE;

/**
 * @author shaohuasu
 * @date 2019-01-03 20:20
 * @since 1.8
 */
@Data
public class QuitGroupResponsePacket extends Packet {

    private Boolean success;

    private String reason;

    private String groupId;

    @Override
    public Byte getCommand() {
        return QUIT_GROUP_RESPONSE;
    }

    public boolean isSuccess() {
        return success;
    }
}
