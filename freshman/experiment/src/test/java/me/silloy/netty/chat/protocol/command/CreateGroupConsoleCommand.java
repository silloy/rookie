package me.silloy.netty.chat.protocol.command;

import me.silloy.netty.chat.protocol.packet.request.CreateGroupRequestPacket;
import io.netty.channel.Channel;

import java.util.Arrays;
import java.util.Scanner;

/**
 * @author shaohuasu
 * @date 2019-01-03 15:29
 * @since 1.8
 */
public class CreateGroupConsoleCommand implements ConsoleCommand {

    private static final String USER_ID_SPLITER = ",";

    @Override
    public void exec(Scanner sc, Channel channel) {
        CreateGroupRequestPacket packet = new CreateGroupRequestPacket();
        System.out.print("【拉人群聊】输入userId列表，userId之间英文逗号隔开: ");
        String userIds = sc.next();
        packet.setUserIdList(Arrays.asList(userIds.split(USER_ID_SPLITER)));
        channel.writeAndFlush(packet);
    }
}
