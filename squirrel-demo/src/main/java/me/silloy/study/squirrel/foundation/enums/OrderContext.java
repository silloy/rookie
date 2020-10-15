package me.silloy.study.squirrel.foundation.enums;

import me.silloy.study.squirrel.foundation.entity.OrderDTO;

/**
 * @author shaohuasu
 * @since 1.8
 */
public class OrderContext {

    public OrderContext(OrderDTO orderDTO) {
        this.orderDTO = orderDTO;
    }

    public OrderContext() {
    }

    public OrderDTO orderDTO;

}

