package com.silverbars.repository;

import com.silverbars.model.Order;
import com.silverbars.model.OrderSummary;
import lombok.extern.slf4j.Slf4j;
import org.h2.api.Aggregate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;


@Profile("cache_persisted")
@Slf4j
@Repository
public class HashMapOrderService implements OrderPersistenceService {

    @Autowired
    EntityInMemoryCache<Order> entityInMemoryCache;


    @Autowired
    private List<Order> initialOrderList;

    @PostConstruct
    public void init() {
        initialOrderList.forEach(o -> {
            log.info("Order loaded into cache:{}", o);
            entityInMemoryCache.save(entityInMemoryCache.getNextId(), o);
        });
    }


    @Override
    public Optional<Order> getOrder(Long id) {
        return Optional.ofNullable(entityInMemoryCache.get(id));
    }

    @Override
    public Order persistOrder(Order newOrder) {
        Long id = entityInMemoryCache.getNextId();
        newOrder = newOrder.toBuilder().id(id).build();
        entityInMemoryCache.save(id, newOrder);
        return newOrder;
    }

    @Override
    public Optional<Order> cancelOrder(Long id) {
        Order order = entityInMemoryCache.remove(id);
        return Optional.ofNullable(order);
    }

    @Override
    public List<Order> getAllOrders() {
        return entityInMemoryCache.getAllValues();
    }

    @Override
    public List<OrderSummary> getSummary() {
        Map<String, Map<Long, Double>> aggregated = entityInMemoryCache.getAllValues().stream()
                .collect(Collectors.groupingBy(Order::getBuySell,
                        Collectors.groupingBy(Order::getOrderPrice, Collectors.summingDouble(Order::getOrderQuantity))
                        )
                );

        return Stream.concat(
                aggregated.get("BUY").entrySet().stream()
                        .sorted(Comparator.comparingLong(Map.Entry<Long, Double>::getKey))
                        .map(e -> OrderSummary.builder().orderPrice(e.getKey()).orderQuantity(e.getValue()).buySell("BUY").build())
                ,aggregated.get("SELL").entrySet().stream()
                        .sorted(Comparator.comparingLong(Map.Entry<Long, Double>::getKey).reversed())
                        .map(e -> OrderSummary.builder().orderPrice(e.getKey()).orderQuantity(e.getValue()).buySell("SELL").build())
        ).collect(toList());
    }


}
