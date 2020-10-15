package me.silloy.netty.simple.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author shaohuasu
 * @date 2018-12-19 13:35
 * @since 1.8
 */
public class NettyClient {

    public final static int MAX_RETRY = 5;


    public static void main(String[] args) throws Exception {
        Bootstrap bootstrap = new Bootstrap();
        NioEventLoopGroup group = new NioEventLoopGroup();
        // 指定线程模型
        bootstrap.group(group)
                // 指定io模型
                .channel(NioSocketChannel.class)
                // io 处理逻辑
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) {
//                        ch.pipeline().addLast(new StringEncoder());
                        ch.pipeline().addLast(new FirstClientHandler());
                    }
                });
        AtomicReference<Integer> index = new AtomicReference<>(0);

        ChannelFuture channelFuture = connect(bootstrap, "127.0.0.1", 8000, MAX_RETRY);
        Channel channel = channelFuture.channel();
//        Thread.sleep(2000);
        channel.writeAndFlush("====>" + index.getAndSet(index.get() + 1) + " time  is  up\n");
        channel.writeAndFlush("====>" + index.getAndSet(index.get() + 1) + " time  is  up with date" + new Date() + "\n");
        while (true) {
            channel.writeAndFlush("====>" + index.getAndSet(index.get() + 1) + " " + new Date() + " : hello world");
            Thread.sleep(2000);
        }
    }


    private static ChannelFuture connect(Bootstrap bootstrap, String host, int port, int retry) {
        return bootstrap.connect(host, port).addListener(future -> {
            if (future.isSuccess()) {
                System.out.println("connect success");
            } else if (retry == 0) {
                System.out.println("retry time is 0, give up connect");
            } else {
                int order = (MAX_RETRY - retry) + 1;
                int delay = 1 << order;
                System.out.println(new Date() + " : connect failure, the" + order + " time reconnect....");
                bootstrap.config().group().schedule(() -> connect(bootstrap, host, port, retry - 1), delay, TimeUnit.SECONDS);
            }
        });
    }
}
