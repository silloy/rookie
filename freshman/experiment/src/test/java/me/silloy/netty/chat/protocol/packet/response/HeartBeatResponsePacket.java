package me.silloy.netty.chat.protocol.packet.response;

import me.silloy.netty.chat.protocol.packet.Packet;
import lombok.Data;

import static me.silloy.netty.chat.protocol.Command.HEARTBEAT_RESPONSE;

/**
 * @author shaohuasu
 * @date 2019-01-04 12:20
 * @since 1.8
 */
@Data
public class HeartBeatResponsePacket extends Packet {
    @Override
    public Byte getCommand() {
        return HEARTBEAT_RESPONSE;
    }
}
