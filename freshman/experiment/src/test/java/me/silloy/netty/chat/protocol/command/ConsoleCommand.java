package me.silloy.netty.chat.protocol.command;


import io.netty.channel.Channel;

import java.util.Scanner;

/**
 * @author shaohuasu
 * @date 2019-01-03 15:17
 * @since 1.8
 */
public interface ConsoleCommand {
    void exec(Scanner sc, Channel channel);
}
