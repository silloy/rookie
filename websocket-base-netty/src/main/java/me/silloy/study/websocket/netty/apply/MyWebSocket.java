package me.silloy.study.websocket.netty.apply;

import me.silloy.study.websocket.netty.yeauty.annotation.ServerEndpoint;
import me.silloy.study.websocket.netty.yeauty.processor.EndpointProcessor;

/**
 * @author shaohuasu
 * @since 1.8
 */
@ServerEndpoint(path = "/ws/abc")
public class MyWebSocket extends EndpointProcessor {

}
