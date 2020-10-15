package me.silloy.netty.chat.protocol.command;

import me.silloy.netty.chat.protocol.packet.request.MessageRequestPacket;
import io.netty.channel.Channel;

import java.util.Scanner;

/**
 * @author shaohuasu
 * @date 2019-01-03 15:29
 * @since 1.8
 */
public class SendToUserConsoleCommand implements ConsoleCommand {
    @Override
    public void exec(Scanner sc, Channel channel) {
        System.out.print("input toUserId: ");
        String touserId = sc.next();
        System.out.print("input message: ");
        String message = sc.next();
        channel.writeAndFlush(new MessageRequestPacket(touserId, message));
    }
}
