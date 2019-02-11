package com.silverbars.repository;

import com.silverbars.model.Order;
import com.silverbars.model.OrderSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

@Profile("db_persisted")
@Repository
public class JPAOrderService implements OrderPersistenceService {

    @Autowired
    OrderRepository orderRepository;

    @PostConstruct
    public void postInit(){
        System.out.println("JPAOrderService -----------------------------------------------");
        System.out.println("orderRepository="+orderRepository);
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
    public Optional<Order> cancelOrder(Long id) {
        return Optional.ofNullable(orderRepository.removeById(id));
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public List<OrderSummary> getSummary() {
        return null;
    }

}