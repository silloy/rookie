package me.silloy.study.websocket.netty.yeauty.processor;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.timeout.IdleStateEvent;
import me.silloy.study.websocket.netty.yeauty.pojo.WebSession;

import java.io.IOException;

/**
 * @author shaohuasu
 * @since 1.8
 */
public class EndpointProcessor {

    public void beforeHandshake(WebSession webSession, Channel channel, FullHttpRequest req) {
        webSession.setSubprotocols("stomp");
    }

    public void onOpen(Channel channel, FullHttpRequest req) {
        System.out.println("新的连接来了");
    }

    public void onClose(WebSession webSession) throws IOException {
    }

    public void onError(WebSession webSession, Throwable throwable) {
        throwable.printStackTrace();
    }

    public void onMessage(WebSession webSession, TextWebSocketFrame message) {
        System.out.println(message);
        webSession.sendText("Hello Netty!");
    }

    public void onBinary(WebSession webSession, BinaryWebSocketFrame bytes) {
//        for (byte b : bytes) {
//            System.out.println(b);
//        }
        webSession.sendBinary(bytes);
    }

    public void onEvent(WebSession webSession, Object evt) {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            switch (idleStateEvent.state()) {
                case READER_IDLE:
                    System.out.println("read idle");
                    break;
                case WRITER_IDLE:
                    System.out.println("write idle");
                    break;
                case ALL_IDLE:
                    System.out.println("all idle");
                    break;
                default:
                    break;
            }
        }
    }


}
