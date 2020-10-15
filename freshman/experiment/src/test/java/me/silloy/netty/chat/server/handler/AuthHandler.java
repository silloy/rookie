package me.silloy.netty.chat.server.handler;

import me.silloy.netty.chat.util.LoginUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author shaohuasu
 * @date 2019-01-03 10:45
 * @since 1.8
 */
@ChannelHandler.Sharable
public class AuthHandler extends ChannelInboundHandlerAdapter {


    public static final AuthHandler INSTANCE = new AuthHandler();

    public AuthHandler() {
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!LoginUtil.hasLogin(ctx.channel())) {
            ctx.channel().close();
        } else {
            // handler 热插拔
            ctx.pipeline().remove(this);
            super.channelRead(ctx, msg);
        }
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        if (LoginUtil.hasLogin(ctx.channel())) {
            System.out.println("登录验证完毕，无需再次验证，authHandler被移除");
        } else {
            System.out.println("无登录验证，关闭连接");
        }
    }
}
