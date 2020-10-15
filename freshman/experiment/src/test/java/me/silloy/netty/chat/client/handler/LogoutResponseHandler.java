package me.silloy.netty.chat.client.handler;

import me.silloy.netty.chat.protocol.packet.request.LogoutRequestPacket;
import me.silloy.netty.chat.util.SessionUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author shaohuasu
 * @date 2018-12-29 17:54
 * @since 1.8
 */
@ChannelHandler.Sharable
public class LogoutResponseHandler extends SimpleChannelInboundHandler<LogoutRequestPacket> {

    public static final ChannelHandler INSTANCE = new LogoutResponseHandler();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LogoutRequestPacket packet) throws Exception {
        SessionUtil.unBindSession(ctx.channel());
    }
}







