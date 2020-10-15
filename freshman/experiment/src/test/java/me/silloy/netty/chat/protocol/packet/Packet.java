package me.silloy.netty.chat.protocol.packet;

import lombok.Data;

/**
 * @author shaohuasu
 * @date 2018-12-26 21:57
 * @since 1.8
 */
@Data
public abstract class Packet {

    private Byte version = 1;

    public abstract Byte getCommand();
}
