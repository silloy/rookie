package me.silloy.netty.chat.protocol.command;

import me.silloy.netty.chat.protocol.packet.response.QuitGroupResponsePacket;
import io.netty.channel.Channel;

import java.util.Scanner;

/**
 * @author shaohuasu
 * @date 2019-01-03 20:26
 * @since 1.8
 */
public class QuitGroupConsoleCommand implements ConsoleCommand {
    @Override
    public void exec(Scanner sc, Channel channel) {
        QuitGroupResponsePacket packet = new QuitGroupResponsePacket();
        System.out.print("enter groupId, quit goup: ");

        String groupId = sc.next();
        packet.setGroupId(groupId);
        channel.writeAndFlush(packet);
    }
}
