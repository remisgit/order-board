package com.silverbars.service;

import com.silverbars.model.Order;
import com.silverbars.model.OrderSummary;
import com.silverbars.repository.OrderRepository;
import com.silverbars.repository.OrderPersistenceService;
import com.silverbars.repository.OrderSummaryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import static java.lang.String.format;

@Slf4j
@Controller
public class RestController {

    @Autowired
    private OrderSummaryRepository orderSummaryRepository;

    @Autowired
    private OrderPersistenceService orderPersitnaceService;

    @GetMapping("/getallorders")
    @ResponseBody
    public List<Order> getAll() {
        log.info("retrieving all orders");
        return orderPersitnaceService.getAllOrders();
    }

    @GetMapping("/getorder")
    @ResponseBody
    public Order getOrderById(@RequestParam(name="orderId") Long orderId) {
        log.info("requested order by id, orderId = {}", orderId);
        Optional<Order> orderOpt = orderPersitnaceService.getOrder(orderId);
        //Order order = orderOpt.orElseThrow(() -> new RuntimeException(format("Order with id=%d not found",orderId)));
        Order order = orderOpt.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,(format("Order with id = [%d] not found", orderId))));
        log.info("requested order by id, orderId = {}, returning order {}", orderId, order);
        return order;
    }

    @PostMapping("/register")
    @ResponseBody
    public Order registerNewOrder(@RequestBody Order newOrder) throws IOException {
        log.info("requested to register a new order, newOrder = {}", newOrder);
        Order saved = orderPersitnaceService.persistOrder(newOrder);
        return saved;
    }

    @GetMapping("/cancel")
    @ResponseBody
    public Order removeOrder(@RequestParam(name="orderId") Long orderId) {
        log.info("requested to cancel order by id = {}", orderId);
        Optional<Order> orderRemovedOpt = orderPersitnaceService.cancelOrder(orderId);

        //Order orderToBeRemoved = orderRemovedOpt.orElseThrow(()-> new RuntimeException("could not cancel order as it does not exist, orderId =" + orderId));
        Order orderToBeRemoved = orderRemovedOpt.orElseThrow(()->
            new ResponseStatusException(HttpStatus.NOT_FOUND, format("could not cancel order as it does not exist, orderId = [%d]", orderId)));
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
