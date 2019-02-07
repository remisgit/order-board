package com.silverbars.repository;

import com.silverbars.model.OrderSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderSummaryRepository extends JpaRepository<OrderSummary, Long > {


    @Query(value = "SELECT ORDER_QUANTITY\n" +
            " ,ORDER_PRICE\n" +
            " ,BUY_SELL\n" +
            "FROM V_ORDER_SUMMARY", nativeQuery = true)

   /*@Query(value = "SELECT new com.silverbars.model.OrderSummary(v.orderQuantity, v.orderPrice, v.buySell)" +
           "FROM V_ORDER_SUMMARY v")*/
    List<OrderSummary> getSummary();

}
