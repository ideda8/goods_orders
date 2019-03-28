package com.da.order.service;

import com.alibaba.fastjson.JSON;
import com.da.cart.Cart;
import com.da.goods.TaskHis;
import com.da.mq.QueueNames;
import com.da.mq.Task;
import com.da.order.Order;
import com.da.order.OrderItem;
import com.da.order.dao.OrderItemRepository;
import com.da.order.dao.OrderRepository;
import com.da.order.dao.TaskHisRepository;
import com.da.order.dao.TaskRepository;
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
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskHisRepository taskHisRepository;

    @Autowired
    private JmsTemplate jmsTemplate;    //mq

    /**
     * 保存订单
     */
    @Transactional
    public void saveOrder(Cart cart){
        //创建订单对象 保存订单
        Order order = new Order();
        orderRepository.save(order);
        //从购物车对象中取出订单项 保存
        List<OrderItem> orderItemsList = cart.getOrderItemsList();
        if(orderItemsList!=null && orderItemsList.size()>0){
            for (OrderItem orderItem : orderItemsList) {
                orderItem.setOrderId(order.getOrderId());
                orderItemRepository.save(orderItem);
            }
        }

        //写入消息列表
        //创建消息对象
        Task task = new Task();
        task.setCreateTime(new Date());
        task.setUpdateTime(new Date());
        task.setMqQueueName(QueueNames.SAVE_ORDER);//消息队列名称
        //消息数据{"orderItemsList":[{"goodsId":"1","num":"1"},{"goodsId":"2","num":"3"}]}
        task.setRequestBody(JSON.toJSONString(orderItemsList));

        //保存消息到数据库
        taskRepository.save(task);

        //发送消息
        jmsTemplate.send(task.getMqQueueName(), new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage(JSON.toJSONString(task));
            }
        });
    }

    @Transactional
    public void deleteTask(Task task){
        //添加历史消息
        TaskHis taskHis = new TaskHis();
        BeanUtils.copyProperties(task,taskHis);
        taskHisRepository.save(taskHis);

        //删除消息
        taskRepository.delete(task);
    }
}
