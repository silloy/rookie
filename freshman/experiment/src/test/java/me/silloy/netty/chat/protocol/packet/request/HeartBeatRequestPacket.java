package me.silloy.netty.chat.protocol.packet.request;

import me.silloy.netty.chat.protocol.packet.Packet;

import static me.silloy.netty.chat.protocol.Command.HEARTBEAT_REQUEST;

/**
 * @author shaohuasu
 * @date 2019-01-04 12:17
 * @since 1.8
 */
public class HeartBeatRequestPacket extends Packet {
    @Override
    public Byte getCommand() {
        return HEARTBEAT_REQUEST;
    }
}
