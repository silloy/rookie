package me.silloy.study.squirrel.foundation.config;

import me.silloy.study.squirrel.foundation.enums.OrderContext;
import me.silloy.study.squirrel.foundation.enums.OrderEvent;
import me.silloy.study.squirrel.foundation.enums.OrderState;
import me.silloy.study.squirrel.foundation.service.OrderService;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.squirrelframework.foundation.fsm.UntypedStateMachine;
import org.squirrelframework.foundation.fsm.annotation.*;
import org.squirrelframework.foundation.fsm.impl.AbstractStateMachine;

/**
 * @author shaohuasu
 * @since 1.8
 */
@States({
        @State(name = "INIT", entryCallMethod = "entryStateInit", exitCallMethod = "exitStateInit", initialState = true),
        @State(name = "WAIT_PAY", entryCallMethod = "entryStateWaitPay", exitCallMethod = "exitStateWaitPay"),
        @State(name = "WAIT_SEND", entryCallMethod = "entryStateWaitSend", exitCallMethod = "exitStateWaitSend"),
        @State(name = "PART_SEND", entryCallMethod = "entryStatePartSend", exitCallMethod = "exitStatePartSend"),
        @State(name = "WAIT_RECEIVE", entryCallMethod = "entryStateWaitReceive", exitCallMethod = "exitStateWaitReceive"),
        @State(name = "COMPLETE", entryCallMethod = "entryStateComplete", exitCallMethod = "exitStateComplete")
})
@Transitions({
        @Transit(from = "INIT", to = "WAIT_PAY", on = "SUBMIT_ORDER", callMethod = "submitOrder"),
        @Transit(from = "WAIT_PAY", to = "WAIT_SEND", on = "PAY", callMethod = "pay"),
        @Transit(from = "WAIT_SEND", to = "PART_SEND", on = "PART_SEND", callMethod = "partSend"),
        @Transit(from = "PART_SEND", to = "WAIT_RECEIVE", on = "SEND", callMethod = "send"),
        @Transit(from = "WAIT_RECEIVE", to = "COMPLETE", on = "COMPLETE", callMethod = "complete")
})
@StateMachineParameters(stateType = OrderState.class, eventType = OrderEvent.class, contextType = OrderContext.class)
public class SubmitOrderStateMachine extends AbstractStateMachine<UntypedStateMachine, Object, Object, Object> implements UntypedStateMachine {

    private OrderService orderService;

    protected ApplicationContext applicationContext;
    //定义构造函数接受ApplicationContext注入([参看New State Machine Instance](http://hekailiang.github.io/squirrel/))
    public SubmitOrderStateMachine(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        // 通过applicationContext注入orderService
        orderService = (OrderService) this.applicationContext.getBean("orderService");
    }

    public void submitOrder(OrderState fromState, OrderState toState, OrderEvent orderEvent, OrderContext orderContext) {
        orderService.submitOrder(toState);
    }

    public void pay(OrderState fromState, OrderState toState, OrderEvent orderEvent, OrderContext orderContext) {
        System.out.println("pay");
    }

    public void partSend(OrderState fromState, OrderState toState, OrderEvent orderEvent, OrderContext orderContext) {
        System.out.println("partSend");
    }

    public void send(OrderState fromState, OrderState toState, OrderEvent orderEvent, OrderContext orderContext) {
        System.out.println("send");
    }

    public void complete(OrderState fromState, OrderState toState, OrderEvent orderEvent, OrderContext orderContext) {
        System.out.println("complete");
    }

    public void entryStateInit(OrderState fromState, OrderState toState, OrderEvent orderEvent, OrderContext orderContext) {
        System.out.println("entryStateInit");
    }

    public void exitStateInit(OrderState fromState, OrderState toState, OrderEvent orderEvent, OrderContext orderContext) {
        System.out.println("exitStateInit");
    }

    public void entryStateWaitPay(OrderState fromState, OrderState toState, OrderEvent orderEvent, OrderContext orderContext) {
        System.out.println("entryStateWaitPay");
    }

    public void exitStateWaitPay(OrderState fromState, OrderState toState, OrderEvent orderEvent, OrderContext orderContext) {
        System.out.println("exitStateWaitPay");
    }

    public void entryStateWaitSend(OrderState fromState, OrderState toState, OrderEvent orderEvent, OrderContext orderContext) {
        System.out.println("entryStateWaitSend");
    }

    public void exitStateWaitSend(OrderState fromState, OrderState toState, OrderEvent orderEvent, OrderContext orderContext) {
        System.out.println("exitStateWaitSend");
    }

    public void entryStatePartSend(OrderState fromState, OrderState toState, OrderEvent orderEvent, OrderContext orderContext) {
        System.out.println("entryStatePartSend");
    }

    public void exitStatePartSend(OrderState fromState, OrderState toState, OrderEvent orderEvent, OrderContext orderContext) {
        System.out.println("exitStatePartSend");
    }

    public void entryStateWaitReceive(OrderState fromState, OrderState toState, OrderEvent orderEvent, OrderContext orderContext) {
        System.out.println("entryStateWaitReceive");
    }

    public void exitStateWaitReceive(OrderState fromState, OrderState toState, OrderEvent orderEvent, OrderContext orderContext) {
        System.out.println("exitStateWaitReceive");
    }

    public void entryStateComplete(OrderState fromState, OrderState toState, OrderEvent orderEvent, OrderContext orderContext) {
        System.out.println("entryStateComplete");
    }

    public void exitStateComplete(OrderState fromState, OrderState toState, OrderEvent orderEvent, OrderContext orderContext) {
        System.out.println("exitStateComplete");
    }

}


