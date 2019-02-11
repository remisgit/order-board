package com.silverbars.repository;

import com.silverbars.model.Order;
import com.silverbars.model.OrderSummary;

import java.util.List;
import java.util.Optional;

public interface OrderPersistenceService {

    Optional<Order> getOrder(Long id);

    Order persistOrder(Order newOrder);

    Optional<Order> cancelOrder(Long id);

    List<Order> getAllOrders();

    List<OrderSummary> getSummary();

}
