package me.silloy.netty.chat.client.handler;

import me.silloy.netty.chat.protocol.packet.response.ListGroupMembersResponsePacket;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author shaohuasu
 * @date 2019-01-03 20:39
 * @since 1.8
 */
@ChannelHandler.Sharable
public class ListGroupMembersResponseHandler extends SimpleChannelInboundHandler<ListGroupMembersResponsePacket> {

    public static final ChannelHandler INSTANCE = new ListGroupMembersResponseHandler();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ListGroupMembersResponsePacket packet) throws Exception {
        System.out.println("Group[" + packet.getGroupId() + "] contains: " + packet.getSessionList());
    }
}
