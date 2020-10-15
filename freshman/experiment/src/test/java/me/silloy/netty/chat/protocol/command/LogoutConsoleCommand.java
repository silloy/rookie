package me.silloy.netty.chat.protocol.command;

import me.silloy.netty.chat.protocol.packet.request.LogoutRequestPacket;
import io.netty.channel.Channel;

import java.util.Scanner;

/**
 * @author shaohuasu
 * @date 2019-01-03 15:29
 * @since 1.8
 */
public class LogoutConsoleCommand implements ConsoleCommand {
    @Override
    public void exec(Scanner sc, Channel channel) {
        LogoutRequestPacket packet = new LogoutRequestPacket();
        channel.writeAndFlush(packet);
    }
}
