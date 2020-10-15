package me.silloy.netty.chat.server.handler;

import me.silloy.netty.chat.protocol.packet.request.ListGroupMembersRequestPacket;
import me.silloy.netty.chat.protocol.packet.response.ListGroupMembersResponsePacket;
import me.silloy.netty.chat.session.Session;
import me.silloy.netty.chat.util.SessionUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import org.assertj.core.util.Lists;

import java.util.List;

/**
 * @author shaohuasu
 * @date 2019-01-03 20:34
 * @since 1.8
 */
@ChannelHandler.Sharable
public class ListGroupMembersRequestHandler extends SimpleChannelInboundHandler<ListGroupMembersRequestPacket> {

    public static final ListGroupMembersRequestHandler INSTANCE = new ListGroupMembersRequestHandler();

    protected ListGroupMembersRequestHandler(){}

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ListGroupMembersRequestPacket packet) throws Exception {
        String groupId = packet.getGroupId();
        ChannelGroup channelGroup = SessionUtil.getChannelGroup(groupId);

        List<Session> sessions = Lists.newArrayList();
        for (Channel channel : channelGroup) {
            Session session = SessionUtil.getSession(channel);
            sessions.add(session);
        }

        ListGroupMembersResponsePacket responsePacket = new ListGroupMembersResponsePacket();
        responsePacket.setGroupId(groupId);
        responsePacket.setSessionList(sessions);
        ctx.channel().writeAndFlush(responsePacket);
    }
}
