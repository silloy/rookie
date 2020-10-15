package me.silloy.netty.chat.server.handler;

import me.silloy.netty.chat.protocol.packet.request.LoginRequestPacket;
import me.silloy.netty.chat.protocol.packet.response.LoginResponsePacket;
import me.silloy.netty.chat.session.Session;
import me.silloy.netty.chat.util.LoginUtil;
import me.silloy.netty.chat.util.SessionUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Date;
import java.util.UUID;

/**
 * @author shaohuasu
 * @date 2018-12-29 17:54
 * @since 1.8
 */
@ChannelHandler.Sharable
public class LoginRequestHandler extends SimpleChannelInboundHandler<LoginRequestPacket> {

    public static final LoginRequestHandler INSTANCE = new LoginRequestHandler();

    protected LoginRequestHandler() {
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginRequestPacket packet) throws Exception {
        LoginResponsePacket loginResponsePacket = new LoginResponsePacket();
        loginResponsePacket.setVersion(packet.getVersion());
        if (valid(packet)) {
            String userId = randomUserId();
            String username = packet.getUsername();
            loginResponsePacket.setUserId(userId);
            loginResponsePacket.setUsername(username);
            loginResponsePacket.setSuccess(true);
            LoginUtil.markAsLogin(ctx.channel());
            SessionUtil.bindSession(new Session(userId, username), ctx.channel());
            System.out.printf("%s : %s : %s : %s \n", new Date(), userId, username, " login success");
        } else {
            loginResponsePacket.setReason("账号密码校验失败");
            loginResponsePacket.setSuccess(false);
            System.out.println(new Date() + ": 登录失败!");
        }
        // 登录响应
//        ByteBuf responseByteBuf = PacketCodeC.INSTANCE.encode(ctx.alloc(), loginResponsePacket);
        ctx.channel().writeAndFlush(loginResponsePacket);
    }


    private static String randomUserId() {
        return UUID.randomUUID().toString().split("-")[0];
    }

    private boolean valid(LoginRequestPacket loginRequestPacket) {
        return true;
    }


    public void channelInactive(ChannelHandlerContext ctx) {
        SessionUtil.unBindSession(ctx.channel());
    }
}







