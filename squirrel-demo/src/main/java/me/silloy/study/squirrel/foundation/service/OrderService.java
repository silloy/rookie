package me.silloy.study.squirrel.foundation.service;

import me.silloy.study.squirrel.foundation.entity.OrderDTO;
import me.silloy.study.squirrel.foundation.enums.OrderState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author shaohuasu
 * @since 1.8
 */
@Service
public class OrderService {

    public static Logger LOGGER = LoggerFactory.getLogger(OrderService.class);

    public int submitOrder(OrderState state) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setState(state);
        LOGGER.info("=========== save.order == {}", orderDTO);
//        orderDTOMapper.insert(orderDTO);
        return 1;
    }

}


