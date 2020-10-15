package me.silloy.netty.chat.protocol.packet.request;

import me.silloy.netty.chat.protocol.packet.Packet;

import static me.silloy.netty.chat.protocol.Command.LOGOUT_REQUEST;

/**
 * @author shaohuasu
 * @date 2019-01-03 16:14
 * @since 1.8
 */
public class LogoutRequestPacket extends Packet {
    @Override
    public Byte getCommand() {
        return LOGOUT_REQUEST;
    }
}
