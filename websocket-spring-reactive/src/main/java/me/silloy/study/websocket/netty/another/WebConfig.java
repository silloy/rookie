//package me.silloy.study.websocket.netty.another;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.reactive.HandlerMapping;
//import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
//import org.springframework.web.reactive.socket.WebSocketHandler;
//import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;
//
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * @author shaohuasu
// * @since 1.8
// */
//@Configuration
//public class WebConfig {
//
//    @Bean
//    public MyWebSocketHandler getMyWebsocketHandler() {
//        return new MyWebSocketHandler();
//    }
//    @Bean
//    public HandlerMapping handlerMapping() {
//        // 对相应的URL进行添加处理器
//        Map<String, WebSocketHandler> map = new HashMap<>();
//        map.put("/hello", getMyWebsocketHandler());
//
//        SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
//        mapping.setUrlMap(map);
//        mapping.setOrder(-1);
//        return mapping;
//    }
//
//    @Bean
//    public WebSocketHandlerAdapter handlerAdapter() {
//        return new WebSocketHandlerAdapter();
//    }
//}
