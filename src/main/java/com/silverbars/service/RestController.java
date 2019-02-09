package com.silverbars.service;

import com.silverbars.model.Order;
import com.silverbars.model.OrderSummary;
import com.silverbars.repository.OrderRepository;
import com.silverbars.repository.OrderSummaryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
public class RestController {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderSummaryRepository orderSummaryRepository;

    @GetMapping("/getall")
    @ResponseBody
    public List<Order> getAll() {
        log.info("retrieving all orders");
        return orderRepository.findAll();
    }

    @PostMapping("/register")
    @ResponseBody
    public Order registerNewOrder(@RequestBody Order newOrder) throws IOException {
        log.info("requested to register a new order, newOrder = {}", newOrder);
        Order saved = orderRepository.save(newOrder);
        return saved;
    }

    @GetMapping("/cancel")
    @ResponseBody
    public Order removeOrder(@RequestParam(name="orderId", required=false) Long orderId) {
        log.info("requested to cancel order by id = {}", orderId);
        Optional<Order> orderToBeRemovedOpt = orderRepository.findById(orderId);
        Order orderToBeRemoved = orderToBeRemovedOpt.orElseThrow(()-> new RuntimeException("could not cancel order as it does not exist, orderId =" + orderId));
        orderRepository.deleteById(orderId);
        return orderToBeRemoved;
    }

    @GetMapping("/aggregate")
    @ResponseBody
    public List<OrderSummary> getSummary() {
        log.info("retrieving aggregated orders");
        List<OrderSummary> summaryList = orderSummaryRepository.getSummary();
        return summaryList;
    }



}
