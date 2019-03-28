package com.da.mq;

import com.alibaba.fastjson.JSON;
import com.da.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class GoodsCountListener {

    @Autowired
    private OrderService orderService;

    @JmsListener(destination = QueueNames.UPDATE_COUNT)
    public void receiveGoodsCount(String taskStr){
        orderService.deleteTask(JSON.parseObject(taskStr, Task.class));
    }
}
