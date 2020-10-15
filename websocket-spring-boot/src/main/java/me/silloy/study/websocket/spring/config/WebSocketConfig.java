package me.silloy.study.websocket.spring.config;

import me.silloy.study.websocket.spring.factory.WebSocketDecoratorFactory;
import me.silloy.study.websocket.spring.handler.PrincipalHandshakeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;


/**
 * WebSocketConfig配置
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Autowired
    private WebSocketDecoratorFactory webSocketDecoratorFactory;

    @Autowired
    private PrincipalHandshakeHandler principalHandshakeHandler;

    @Autowired
    private MyHandShakeInterceptor myHandShakeInterceptor;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        /**
         * myUrl表示 你前端到时要对应url映射
         */
        registry.addEndpoint("/myUrl")
                .setAllowedOrigins("*")
                .setHandshakeHandler(principalHandshakeHandler)
//                .withSockJS()
        ;
        registry.addEndpoint("/limi-websocket")
                .setAllowedOrigins("*")
                .setHandshakeHandler(principalHandshakeHandler)
//                .withSockJS()
        ;

        // 将 "/gs-guide-websocket" 注册为一个 STOMP 端点。客户端在订阅或发布消息到目的地路径前，要连接到该端点
        registry.addEndpoint("/gs-guide-websocket")
            .addInterceptors(myHandShakeInterceptor)
            .setAllowedOrigins("*")
//            .withSockJS()
        ;
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        /**
         *
         * 开头的消息都会发送到STOMP代理中，根据所选择的STOMP代理不同，目的地的可选前缀也会有所限制；
         * queue 点对点
         * topic 广播
         * user 点对点前缀
         */
        registry.enableSimpleBroker("/queue", "/topic");
        registry.setUserDestinationPrefix("/user");

        // 以 /app 开头的消息都会被路由到带有@MessageMapping 或 @SubscribeMapping 注解的方法中；
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
        registration.addDecoratorFactory(webSocketDecoratorFactory);
    }
}
