package com.da.goods.service;

import com.alibaba.fastjson.JSON;
import com.da.goods.Goods;
import com.da.goods.TaskHis;
import com.da.goods.dao.GoodsRepository;
import com.da.goods.dao.TaskHisRepository;
import com.da.mq.QueueNames;
import com.da.mq.Task;
import com.da.order.OrderItem;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Service
public class GoodsService {

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private TaskHisRepository taskHisRepository;

    @Autowired
    private JmsTemplate jmsTemplate;    //mq


    //从MQ接受Task的Json 转成Task对象传入更新
    @Transactional
    public void updateCount(Task task){
        //处理 只有一条消息被处理成功
        //更具task的id获取taskhis
        TaskHis th = taskHisRepository.findOne(task.getId());
        if(th!=null){
            //有历史消息 表示task已经被处理了
            return;
        }


        //扣减库存
        //获取消息数据 哪个商品 买了多少件
        String requestBody = task.getRequestBody();
        List<OrderItem> orderItemsList = JSON.parseArray(requestBody, OrderItem.class);
        if(orderItemsList!=null && orderItemsList.size()>0){
            for (OrderItem oi : orderItemsList) {
                Long goodsId = oi.getGoodsId();
                Integer num = oi.getNum();

                //判断剩余数量
                Goods g = goodsRepository.findOne(goodsId);
                if(g.getCount()<num){
                    throw new RuntimeException("库存不足");
                }

                goodsRepository.updateCount(goodsId, num);
            }
        }

        //写入消息表
        TaskHis taskHis = new TaskHis();
        BeanUtils.copyProperties(task,taskHis);
        taskHisRepository.save(taskHis);

        //写回成功消息
        Task taskSuc = new Task();
        taskSuc.setId(task.getId());
        taskSuc.setCreateTime(new Date());
        taskSuc.setUpdateTime(new Date());
        taskSuc.setMqQueueName(QueueNames.UPDATE_COUNT);
        taskSuc.setRequestBody(task.getRequestBody());

        jmsTemplate.send(taskSuc.getMqQueueName(), new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage(JSON.toJSONString(taskSuc));
            }
        });

    }

}
