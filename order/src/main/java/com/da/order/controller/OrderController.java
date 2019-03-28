package com.da.order.controller;

import com.da.cart.Cart;
import com.da.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/save")
    public String saveOrder(@RequestBody Cart cart){    //参数@RequestBody 传入JSON
        orderService.saveOrder(cart);

        return "success";
    }



}
