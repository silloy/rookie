package me.silloy.study.squirrel.foundation.entity;

import me.silloy.study.squirrel.foundation.enums.OrderState;

import java.util.Date;

/**
 * @author shaohuasu
 * @since 1.8
 */
public class OrderDTO {

    private Integer id;

    private OrderState state;

    private Date createTime;

    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public OrderState getState() {
        return state;
    }

    public void setState(OrderState state) {
        this.state = state;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "OrderDTO{" +
                "id=" + id +
                ", state='" + state + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }

    public OrderDTO(OrderState state) {
        this.state = state;
    }

    public OrderDTO() {
    }
}



