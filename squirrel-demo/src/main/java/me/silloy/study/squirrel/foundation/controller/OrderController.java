package me.silloy.study.squirrel.foundation.controller;

import me.silloy.study.squirrel.foundation.config.OrderStateMachineEngine;
import me.silloy.study.squirrel.foundation.config.SubmitOrderStateMachine;
import me.silloy.study.squirrel.foundation.entity.OrderDTO;
import me.silloy.study.squirrel.foundation.enums.OrderContext;
import me.silloy.study.squirrel.foundation.enums.OrderEvent;
import me.silloy.study.squirrel.foundation.enums.OrderState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author shaohuasu
 * @since 1.8
 */
@RestController
@RequestMapping(value = "/order/")
public class OrderController {

    // 注入状态机
    @Autowired
    OrderStateMachineEngine orderStateMachineEngine;
//    SubmitOrderStateMachine submitOrderStateMachine;

    @RequestMapping("/test")
    public void test() {
        OrderDTO orderDTO = new OrderDTO(OrderState.INIT);
        OrderContext orderContext = new OrderContext(orderDTO);
        orderStateMachineEngine.fire(OrderEvent.SUBMIT_ORDER, orderContext);
//        submitOrderStateMachine.fire(OrderEvent.SUBMIT_ORDER, orderContext);

    }

}

