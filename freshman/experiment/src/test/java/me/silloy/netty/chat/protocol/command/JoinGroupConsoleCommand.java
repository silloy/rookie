package me.silloy.netty.chat.protocol.command;

import me.silloy.netty.chat.protocol.packet.request.JoinGroupRequestPacket;
import io.netty.channel.Channel;

import java.util.Scanner;

/**
 * @author shaohuasu
 * @date 2019-01-03 19:59
 * @since 1.8
 */
public class JoinGroupConsoleCommand implements ConsoleCommand {
    @Override
    public void exec(Scanner sc, Channel channel) {
        JoinGroupRequestPacket packet = new JoinGroupRequestPacket();
        System.out.print("enter groupId, join goup: ");

        String groupId = sc.next();
        packet.setGroupId(groupId);
        channel.writeAndFlush(packet);
    }
}
