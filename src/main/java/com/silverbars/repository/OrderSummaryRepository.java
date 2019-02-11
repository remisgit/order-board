package com.silverbars.repository;

import com.silverbars.model.OrderSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderSummaryRepository extends JpaRepository<OrderSummary, Long > {

    @Query(value = "SELECT " +
            "rownum AS ID," +
            "ORDER_QUANTITY\n" +
            " ,ORDER_PRICE\n" +
            " ,BUY_SELL\n" +
            "FROM V_ORDER_SUMMARY", nativeQuery = true)
    List<OrderSummary> getSummary();

}
