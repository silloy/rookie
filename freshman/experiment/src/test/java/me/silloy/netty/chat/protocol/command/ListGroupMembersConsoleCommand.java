package me.silloy.netty.chat.protocol.command;

import me.silloy.netty.chat.protocol.packet.request.ListGroupMembersRequestPacket;
import io.netty.channel.Channel;

import java.util.Scanner;

/**
 * @author shaohuasu
 * @date 2019-01-03 19:59
 * @since 1.8
 */
public class ListGroupMembersConsoleCommand implements ConsoleCommand {
    @Override
    public void exec(Scanner sc, Channel channel) {
        ListGroupMembersRequestPacket packet = new ListGroupMembersRequestPacket();
        System.out.print("enter groupId, list group members: ");

        String groupId = sc.next();
        packet.setGroupId(groupId);
        channel.writeAndFlush(packet);
    }
}
