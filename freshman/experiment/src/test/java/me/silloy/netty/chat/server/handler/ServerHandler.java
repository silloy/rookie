//package me.silloy.netty.chat.server;
//
//import me.silloy.netty.chat.protocol.*;
//import io.netty.buffer.ByteBuf;
//import io.netty.channel.ChannelHandler;
//import io.netty.channel.ChannelHandlerContext;
//import io.netty.channel.ChannelInboundHandlerAdapter;
//import io.netty.channel.SimpleChannelInboundHandler;
//
//import java.util.Date;
//
///**
// * @author shaohuasu
// * @date 2018-12-19 16:52
// * @since 1.8
// */
//@ChannelHandler.Sharable
//public class ServerHandler extends ChannelInboundHandlerAdapter {
//
//    @Override
//    public void channelRead(ChannelHandlerContext ctx, Object msg) {
//        ByteBuf requestByteBuf = (ByteBuf) msg;
//
//        Packet packet = PacketCodeC.INSTANCE.decode(requestByteBuf);
//
//        if (packet instanceof LoginRequestPacket) {
//            // 登录流程
//            LoginRequestPacket loginRequestPacket = (LoginRequestPacket) packet;
//
//            LoginResponsePacket loginResponsePacket = new LoginResponsePacket();
//            loginResponsePacket.setVersion(packet.getVersion());
//            if (valid(loginRequestPacket)) {
//                loginResponsePacket.setSuccess(true);
//                System.out.println(new Date() + ": 登录成功!");
//            } else {
//                loginResponsePacket.setReason("账号密码校验失败");
//                loginResponsePacket.setSuccess(false);
//                System.out.println(new Date() + ": 登录失败!");
//            }
//            // 登录响应
//            ByteBuf responseByteBuf = PacketCodeC.INSTANCE.encode(ctx.alloc(), loginResponsePacket);
//            ctx.channel().writeAndFlush(responseByteBuf);
//        }
//
//        if (packet instanceof MessageRequestPacket) {
//            MessageRequestPacket messageRequestPacket = (MessageRequestPacket) packet;
//            System.out.println(new Date() + ": receive client message: " + messageRequestPacket.getMessage());
//
//            MessageResponsePacket messageResponsePacket = new MessageResponsePacket();
//            messageResponsePacket.setMessage("server reply [ " + messageRequestPacket.getMessage() + " ]");
//            ByteBuf buf = PacketCodeC.INSTANCE.encode(ctx.alloc(), messageResponsePacket);
//            ctx.channel().writeAndFlush(buf);
//        }
//    }
//
//
//
//    private boolean valid(LoginRequestPacket loginRequestPacket) {
//        return true;
//    }
//}
