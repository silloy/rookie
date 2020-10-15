package me.silloy.netty.chat.client.handler;

import me.silloy.netty.chat.protocol.packet.response.CreateGroupResponsePacket;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author shaohuasu
 * @date 2019-01-03 16:07
 * @since 1.8
 */
@ChannelHandler.Sharable
public class CreateGroupResponseHandler extends SimpleChannelInboundHandler<CreateGroupResponsePacket> {

    public static final ChannelHandler INSTANCE = new CreateGroupResponseHandler();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CreateGroupResponsePacket packet) throws Exception {
        System.out.print("Group created!  id=[" + packet.getGroupId() + "],");
        System.out.println("Group member: " + packet.getUserNameList());
    }
}
