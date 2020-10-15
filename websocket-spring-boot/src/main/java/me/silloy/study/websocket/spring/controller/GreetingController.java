package me.silloy.study.websocket.spring.controller;

import me.silloy.study.websocket.spring.entity.Greeting;
import me.silloy.study.websocket.spring.entity.HelloMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

/**
 * @author shaohuasu
 * @since 1.8
 */
@Controller
public class GreetingController {

  /**
   * 接收客户端发来的消息，参数就是消息本身message
   * @MessageMapping 或者 @SubscribeMapping 注解可以处理客户端发送过来的消息，并选择方法是否有返回值。
   * @MessageMapping 指定目的地是“/app/ws/chat”（“/app”前缀是隐含的，因为我们将其配置为应用的目的地前缀）。
   * 通信协议可以自定义——可自定义参数的格式
   * 可以接收json格式的数据,传递json数据时不需要添加额外注解@Requestbody
   * 消息发送者不是从前端传递过来的，而是从springsecurity中获取的,防止前端冒充
   * 如果 @MessageMapping 注解的控制器方法有返回值的话，返回值会被发送到消息代理，只不过会添加上"/topic"前缀。
   * 通过为方法添加@SendTo注解，重载目的地
   *
   * https://user-gold-cdn.xitu.io/2018/4/7/162a06eacdd1d137?imageView2/0/w/1280/h/960/format/webp/ignore-error/1
   *
   * @return
   * @throws Exception
   */
  @MessageMapping("/hello")
  @SendTo("/topic/greetings")
  public Greeting greeting(HelloMessage message
//      , Principal principal
  ) throws Exception {
    Thread.sleep(1000); // simulated delay
    return new Greeting("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");
  }
}
