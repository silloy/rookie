package me.silloy.netty.chat.protocol.command;

import me.silloy.netty.chat.protocol.packet.request.LoginRequestPacket;
import io.netty.channel.Channel;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * @author shaohuasu
 * @date 2019-01-03 15:41
 * @since 1.8
 */
public class LoginConsoleCommand implements ConsoleCommand {
    @Override
    public void exec(Scanner sc, Channel channel) {
        LoginRequestPacket packet = new LoginRequestPacket();
        System.out.print("input username: ");
        String username = sc.nextLine();
        packet.setUsername(username);
        packet.setPassword("pwd");
        channel.writeAndFlush(packet);
        waitForLoginResponse();
    }


    private static void waitForLoginResponse() {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
