package me.silloy.study.squirrel.foundation.enums;

/**
 * @author shaohuasu
 * @since 1.8
 */
public enum OrderState {

    INIT,

    WAIT_PAY,

    WAIT_SEND,

    PART_SEND,

    WAIT_RECEIVE,

    COMPLETE,

    CANCELED;

    public static OrderState getState(String state) {
        for (OrderState orderState : OrderState.values()) {
            if (orderState.name().equalsIgnoreCase(state)) {
                return orderState;
            }
        }
        return null;
    }
}

