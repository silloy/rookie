package me.silloy.netty.chat.client.handler;

import me.silloy.netty.chat.protocol.packet.response.JoinGroupResponsePacket;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author shaohuasu
 * @date 2019-01-03 20:11
 * @since 1.8
 */
@ChannelHandler.Sharable
public class JoinGroupResponseHandler extends SimpleChannelInboundHandler<JoinGroupResponsePacket> {

    public static final ChannelHandler INSTANCE = new JoinGroupResponseHandler();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, JoinGroupResponsePacket packet) throws Exception {
        if (packet.isSuccess()) {
            System.out.println("Join group[ " + packet.getGroupId() + " ] success!");
        } else
            System.err.println("Join group[ " + packet.getGroupId() + " ] failure, reason=" + packet.getReason());
    }
}
