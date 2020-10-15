package me.silloy.netty.chat.client.handler;

import me.silloy.netty.chat.protocol.packet.request.LoginRequestPacket;
import me.silloy.netty.chat.protocol.packet.response.LoginResponsePacket;
import me.silloy.netty.chat.session.Session;
import me.silloy.netty.chat.util.LoginUtil;
import me.silloy.netty.chat.util.SessionUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Date;

/**
 * @author shaohuasu
 * @date 2018-12-29 17:54
 * @since 1.8
 */
@ChannelHandler.Sharable
public class LoginResponseHandler extends SimpleChannelInboundHandler<LoginResponsePacket> {
    public static final ChannelHandler INSTANCE = new LoginResponseHandler();

//    @Override
//    public void channelActive(ChannelHandlerContext ctx) {
//        System.out.println(new Date() + ": 客户端开始登录");
//        LoginRequestPacket loginRequestPacket = new LoginRequestPacket();
//        loginRequestPacket.setUserId(UUID.randomUUID().toString().replace("-", "").toUpperCase());
//        loginRequestPacket.setUsername("flash");
//        loginRequestPacket.setPassword("pwd");
//        // 写数据
//        ctx.channel().writeAndFlush(loginRequestPacket);
//    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginResponsePacket packet) throws Exception {

        String userId = packet.getUserId();
        String userName = packet.getUsername();

        if (packet.isSuccess()) {
            System.out.println("[" + userName + "]登录成功，userId 为: " + packet.getUserId());
            SessionUtil.bindSession(new Session(userId, userName), ctx.channel());
        } else {
            System.out.println("[" + userName + "]登录失败，原因：" + packet.getReason());
        }

        if (packet.isSuccess()) {
            LoginUtil.markAsLogin(ctx.channel());
            System.out.println("Login: " + LoginUtil.hasLogin(ctx.channel()));
            System.out.println(new Date() + ": 客户端登录成功");
        } else {
            System.out.println(new Date() + ": 客户端登录失败，原因：" + packet.getReason());
        }
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        System.out.println("client has closed!");
    }


    private boolean valid(LoginRequestPacket loginRequestPacket) {
        return true;
    }
}







