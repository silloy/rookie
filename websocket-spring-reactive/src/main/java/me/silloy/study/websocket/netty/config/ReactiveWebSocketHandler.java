package me.silloy.study.websocket.netty.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

import static java.time.LocalDateTime.now;
import static java.util.UUID.randomUUID;

@Component("reactiveWebSocketHandler")
public class ReactiveWebSocketHandler implements WebSocketHandler {
    private final Logger log = LoggerFactory.getLogger(ReactiveWebSocketHandler.class);

    private static final ObjectMapper json = new ObjectMapper();

    private Flux<String> eventFlux = Flux.generate(sink -> {
        System.out.println("==========eventFlux============");
        Event event = new Event(randomUUID().toString(), now().toString());
        try {
            sink.next(json.writeValueAsString(event));
        } catch (JsonProcessingException e) {
            sink.error(e);
        }
    });

    private Flux<String> intervalFlux = Flux.interval(Duration.ofMillis(1000L))
            .zipWith(eventFlux, (time, event) -> event);


    // 通过返回的Body 判断
    // 返回示例: {"seq":"1566276523281-585638","cmd":"heartbeat","response":{"code":200,"codeMsg":"Success","data":null}}
    // code 取body中的返回code
    @Override
    public Mono<Void> handle(WebSocketSession session) {
        System.out.println("==========handle============");
//        return session.send(intervalFlux
//          .map(session::textMessage))
//          .and(session.receive().map(WebSocketMessage::getPayloadAsText).log());
//        return session.send(Flux
//                .interval(Duration.ofSeconds(1))
//                .map(Object::toString)
//                .map(session::textMessage))
//                .and(session.receive()
//                        .map(WebSocketMessage::getPayloadAsText).doOnNext(msg -> log.info("prime#: " + msg)));
        return session.send(session.receive().doOnNext(WebSocketMessage::retain));
//        return session.receive()
//                .doOnNext(message -> {
//                    log.info("message:" + message);
//                })
////                .concatMap(message ->
////                })
//                .then();
    }

//    WebSocketHandler webSocketHandler() {
//        return session ->
//                session.send(
//                        Flux.interval(Duration.ofSeconds(1))
//                                .map(n -> n.toString())
//                                .map(session::textMessage)
//                ).and(session.receive()
//                        .map(WebSocketMessage::getPayloadAsText)
//                        .doOnNext(msg -> log.info("Prime#: " + msg))
//                );
//    }
}
