package me.silloy.netty.chat.client.handler;

import me.silloy.netty.chat.protocol.packet.response.GroupMessageResponsePacket;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author shaohuasu
 * @date 2019-01-03 20:39
 * @since 1.8
 */
@ChannelHandler.Sharable
public class GroupMessageResponseHandler extends SimpleChannelInboundHandler<GroupMessageResponsePacket> {

    public static final ChannelHandler INSTANCE = new GroupMessageResponseHandler();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupMessageResponsePacket responsePacket) throws Exception {
        System.out.println("Group[" + responsePacket.getFromGroupId() + "] message: " + responsePacket.getMessage());
    }
}
