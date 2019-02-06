package com.silverbars.service;

import com.silverbars.model.OrderEntity;
import com.silverbars.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
public class RestResources {

    @Autowired
    OrderRepository orderRepository;

    @GetMapping("/sample")
    @ResponseBody
    public /*String */OrderEntity sample() {
        log.info("requested sample ");
        return OrderEntity.builder().orderQuantity(3f).buySell("BUY").userId("user33").build();
    }

    @PostMapping("/register")
    @ResponseBody
    public /*String */OrderEntity sayHello(@RequestBody OrderEntity newOrder) {
        log.info("requested to register new order = {}", newOrder);
        OrderEntity saved = orderRepository.save(newOrder);
        return saved;
    }


}
