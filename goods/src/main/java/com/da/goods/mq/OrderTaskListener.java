package com.da.goods.mq;

import com.alibaba.fastjson.JSON;
import com.da.goods.service.GoodsService;
import com.da.mq.QueueNames;
import com.da.mq.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class OrderTaskListener {

    @Autowired
    private GoodsService goodsService;

    @JmsListener(destination = QueueNames.SAVE_ORDER)
    public void receiveOrderTask(String taskStr){
        System.out.println(taskStr);
        goodsService.updateCount(JSON.parseObject(taskStr, Task.class));
    }
}
