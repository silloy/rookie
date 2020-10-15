package me.silloy.netty.chat.client;

import me.silloy.netty.chat.client.handler.*;
import me.silloy.netty.chat.protocol.PacketDecoder;
import me.silloy.netty.chat.protocol.PacketEncoder;
import me.silloy.netty.chat.protocol.Spliter;
import me.silloy.netty.chat.protocol.command.ConsoleCommandManager;
import me.silloy.netty.chat.protocol.command.LoginConsoleCommand;
import me.silloy.netty.chat.server.handler.IMIdleStateHandler;
import me.silloy.netty.chat.util.SessionUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * @author shaohuasu
 * @date 2018-12-19 13:35
 * @since 1.8
 */
public class NettyChatClient {

    private static final int MAX_RETRY = 5;
    private static final String HOST = "127.0.0.1";
    private static final int PORT = 8001;


    public static void main(String[] args) {
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap
                .group(workerGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) {
//                        ch.pipeline().addLast(new ClientHandler());
                        ch.pipeline().addLast(new IMIdleStateHandler());
                        ch.pipeline().addLast(new Spliter());
                        ch.pipeline().addLast(new PacketDecoder());
                        ch.pipeline().addLast(LoginResponseHandler.INSTANCE);
                        ch.pipeline().addLast(MessageResponseHandler.INSTANCE);
                        ch.pipeline().addLast(CreateGroupResponseHandler.INSTANCE);
                        ch.pipeline().addLast(JoinGroupResponseHandler.INSTANCE);
                        ch.pipeline().addLast(QuitGroupResponseHandler.INSTANCE);
                        ch.pipeline().addLast(ListGroupMembersResponseHandler.INSTANCE);
                        ch.pipeline().addLast(GroupMessageResponseHandler.INSTANCE);
                        ch.pipeline().addLast(LogoutResponseHandler.INSTANCE);
                        ch.pipeline().addLast(new PacketEncoder());
                    }
                });

        connect(bootstrap, HOST, PORT, MAX_RETRY);
    }

    private static void connect(Bootstrap bootstrap, String host, int port, int retry) {
        bootstrap.connect(host, port).addListener(future -> {
            if (future.isSuccess()) {
                System.out.println(new Date() + ": 连接成功!");
                // 启动控制台线程
                Channel channel = ((ChannelFuture) future).channel();
                startConsoleThread(channel);
            } else if (retry == 0) {
                System.err.println("重试次数已用完，放弃连接！");
            } else {
                // 第几次重连
                int order = (MAX_RETRY - retry) + 1;
                // 本次重连的间隔
                int delay = 1 << order;
                System.err.println(new Date() + ": 连接失败，第" + order + "次重连……");
                bootstrap.config().group().schedule(() -> connect(bootstrap, host, port, retry - 1), delay, TimeUnit
                        .SECONDS);
            }
        });
    }


    private static void startConsoleThread(Channel channel) {
        ConsoleCommandManager manager = new ConsoleCommandManager();
        LoginConsoleCommand loginConsoleCommand = new LoginConsoleCommand();

        Scanner sc = new Scanner(System.in);
        new Thread(() -> {
            while (!Thread.interrupted()) {
                if (!SessionUtil.hasLogin(channel)) {
                    loginConsoleCommand.exec(sc, channel);
                } else {
                    manager.exec(sc, channel);
                }
            }
        }).start();
    }


//    private static void startConsoleThread(Channel channel) {
//        new Thread(() -> {
//            while (!Thread.interrupted()) {
////                if (LoginUtil.hasLogin(channel)) {
//                    System.out.println("output to server: ");
//                    Scanner sc = new Scanner(System.in);
//                    String line = null;
//                    try {
//                        line = sc.nextLine();
//                    } catch (NoSuchElementException e) {
//                        System.out.println("-----------> Thread isInterrupted! <-----------");
//                        break;
//                    }
//                    channel.writeAndFlush(new MessageRequestPacket(line));
////                }
//            }
//        }).start();
//    }
}
