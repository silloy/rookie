package me.silloy.netty.chat.server.handler;

import me.silloy.netty.chat.protocol.packet.request.HeartBeatRequestPacket;
import me.silloy.netty.chat.protocol.packet.response.HeartBeatResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author shaohuasu
 * @date 2019-01-04 12:16
 * @since 1.8
 */
public class HeartBeatRequestHandler extends SimpleChannelInboundHandler<HeartBeatRequestPacket> {

    public static final HeartBeatRequestHandler INSTANCE = new HeartBeatRequestHandler();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HeartBeatRequestPacket msg) throws Exception {
        ctx.writeAndFlush(new HeartBeatResponsePacket());
    }
}
