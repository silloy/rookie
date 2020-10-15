package me.silloy.netty.simple.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

public class NettyServer {
    public static void main(String[] args) {

        /*
        boss线程池执行的任务是接受客户端的连接请求，并建立channel。
        正常情况下一个线程就够了，当然多个线程也是可以的，boss线程池的线程越多，同一时间能够连接上的客户端就越多。
        work线程池执行的任务是每个线程会监听多个channel的事件，对应channel触发监听的事件时候由对应的work线程处理，
        work线程池的线程越多，同一时间，能够处理的事件就越多（连接、读写等事件）
         */
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();
        serverBootstrap
                /*
                    配置线程模型
                 */
                .group(boss, worker)
                /*
                    io模型
                 */
                .channel(NioServerSocketChannel.class)
                /*
                    用于处理连续读写处理逻辑
                    handler()用于指定在服务端启动过程中的一些逻辑，通常情况下呢，我们用不着这个方法。
                 */
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    protected void initChannel(NioSocketChannel ch) {
                        ch.pipeline().addLast(new StringDecoder());
//                        ch.pipeline().addLast(new SimpleChannelInboundHandler<String>() {
//                            @Override
//                            protected void channelRead0(ChannelHandlerContext ctx, String msg) {
//                                System.out.println(msg);
//                            }
//                        });
                        ch.pipeline().addLast(new FirstServerHandler());
                    }
                })
                // 连接设置 表示是否开启TCP底层心跳机制，true为开启
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.TCP_NODELAY, true)
                // 表示系统用于临时存放已完成三次握手的请求的队列的最大长度，如果连接建立频繁，服务器处理创建新连接较慢，可以适当调大这个参数
                .option(ChannelOption.SO_BACKLOG, 1024);


        bind(serverBootstrap, 8000);
    }


    /**
     * 动态绑定端口
     *
     * @param serverBootstrap 引导
     * @param port            端口
     */
    private static void bind(final ServerBootstrap serverBootstrap, final int port) {
          /*
                    监听端口是否绑定成功
                 */
        serverBootstrap.bind(port).addListener(
                future -> {
                    if (future.isSuccess()) {
                        System.out.println("port [ " + port + " ] bind success");
                    } else {
                        System.out.println("port [ " + port + " ] bind fail");
                        bind(serverBootstrap, port + 1);
                    }
                }
        );
    }
}
