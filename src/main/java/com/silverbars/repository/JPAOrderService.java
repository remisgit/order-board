package com.silverbars.repository;

import com.silverbars.model.Order;
import com.silverbars.model.OrderSummary;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

@Profile("db_persisted")
@Slf4j
@Repository
public class JPAOrderService implements OrderPersistenceService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderSummaryRepository orderSummaryRepository;

    @Autowired
    private List<Order> initialOrderList;

    @PostConstruct
    public void postInit() {
        log.info("initialisedin db with initial order list of size {}", initialOrderList.size());
        initialOrderList.forEach(o -> {
            orderRepository.save(o.toBuilder().id(null).build());//force to get id from a sequence
        });
    }

    @Override
    public Optional<Order> getOrder(Long id) {
        return orderRepository.findById(id);
    }

    @Override
    public Order persistOrder(Order newOrder) {
        return orderRepository.save(newOrder);
    }

    @Override
    public void cancelOrder(Long id) {
        orderRepository.deleteById(id);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public List<OrderSummary> getSummary() {
        return orderSummaryRepository.getSummary();
    }

}