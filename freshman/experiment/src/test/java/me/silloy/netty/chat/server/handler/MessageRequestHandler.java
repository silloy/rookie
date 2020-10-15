package me.silloy.netty.chat.server.handler;

import me.silloy.netty.chat.protocol.packet.request.MessageRequestPacket;
import me.silloy.netty.chat.protocol.packet.response.MessageResponsePacket;
import me.silloy.netty.chat.session.Session;
import me.silloy.netty.chat.util.SessionUtil;
import io.netty.channel.Channel;
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
public class MessageRequestHandler extends SimpleChannelInboundHandler<MessageRequestPacket> {


    public static final MessageRequestHandler INSTANCE = new MessageRequestHandler();

    public MessageRequestHandler() {
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageRequestPacket packet) throws Exception {
        Session session = SessionUtil.getSession(ctx.channel());

        System.out.println(new Date() + ": receive client message: " + packet.getMessage());

        MessageResponsePacket messageResponsePacket = new MessageResponsePacket();
        messageResponsePacket.setMessage("server reply [ " + packet.getMessage() + " ]");
        messageResponsePacket.setFromUserId(session.getUserId());
        messageResponsePacket.setFromUserName(session.getUsername());

        Channel toUserChannel = SessionUtil.getChannel(packet.getToUserId());

        if (toUserChannel != null && SessionUtil.hasLogin(toUserChannel)) {
            toUserChannel.writeAndFlush(messageResponsePacket);
        } else {
            System.out.println("[ " + packet.getToUserId() + " ] 不在线，发送失败!");
        }

//        ByteBuf buf = PacketCodeC.INSTANCE.encode(ctx.alloc(), messageResponsePacket);
//        ctx.channel().writeAndFlush(messageResponsePacket);
    }
}
