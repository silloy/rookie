package me.silloy.netty.chat.client.handler;

import me.silloy.netty.chat.protocol.packet.response.MessageResponsePacket;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Date;

/**
 * @author shaohuasu
 * @date 2019-01-02 10:17
 * @since 1.8
 */
@ChannelHandler.Sharable
public class MessageResponseHandler extends SimpleChannelInboundHandler<MessageResponsePacket> {
    public static final ChannelHandler INSTANCE = new MessageResponseHandler();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageResponsePacket packet) throws Exception {
        String fromUserId = packet.getFromUserId();
        String fromUsername = packet.getFromUserName();

        System.out.printf("\n%s : %s : %s -> %s\n", new Date(), fromUserId, fromUsername, packet.getMessage());
//        System.out.printf(new Date() + ": receive message from server: " + packet.getMessage());
    }
}
