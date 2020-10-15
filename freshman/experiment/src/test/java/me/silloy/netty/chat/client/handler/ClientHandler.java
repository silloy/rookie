//package me.silloy.netty.chat.client;
//
//import me.silloy.netty.chat.protocol.*;
//import me.silloy.netty.chat.util.LoginUtil;
//import io.netty.buffer.ByteBuf;
//import io.netty.channel.ChannelHandlerContext;
//import io.netty.channel.ChannelInboundHandlerAdapter;
//
//import java.util.Date;
//import java.util.UUID;
//
///**
// * @author shaohuasu
// * @date 2018-12-26 22:43
// * @since 1.8
// */
//public class ClientHandler extends ChannelInboundHandlerAdapter {
//
//    @Override
//    public void channelActive(ChannelHandlerContext ctx) {
//        System.out.println(new Date() + ": 客户端开始登录");
//
//        // 创建登录对象
//        LoginRequestPacket loginRequestPacket = new LoginRequestPacket();
//        loginRequestPacket.setUserId(UUID.randomUUID().toString().replace("-", "").toUpperCase());
//        loginRequestPacket.setUsername("flash");
//        loginRequestPacket.setPassword("pwd");
//
//        // 编码
//        ByteBuf buffer = PacketCodeC.INSTANCE.encode(ctx.alloc(), loginRequestPacket);
//
//        // 写数据
//        ctx.channel().writeAndFlush(buffer);
//    }
//
//
//    /**
//     * 客户端处理服务端数据
//     * @param ctx
//     * @param msg
//     */
//    @Override
//    public void channelRead(ChannelHandlerContext ctx, Object msg) {
//        ByteBuf byteBuf = (ByteBuf) msg;
//
//        Packet packet = PacketCodeC.INSTANCE.decode(byteBuf);
//
//        if (packet instanceof LoginResponsePacket) {
//            LoginResponsePacket loginResponsePacket = (LoginResponsePacket) packet;
//            if (loginResponsePacket.isSuccess()) {
//                LoginUtil.markAsLogin(ctx.channel());
//                System.out.println(new Date() + ": 客户端登录成功");
//            } else {
//                System.out.println(new Date() + ": 客户端登录失败，原因：" + loginResponsePacket.getReason());
//            }
//        }
//
//        if (packet instanceof MessageResponsePacket) {
//            MessageResponsePacket responsePacket = (MessageResponsePacket) packet;
//            System.out.println(new Date() + ": receive message from server: " + responsePacket.getMessage());
//        }
//    }
//}
