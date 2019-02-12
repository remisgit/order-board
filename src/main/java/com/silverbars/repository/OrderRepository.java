package com.silverbars.repository;

import com.silverbars.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

public interface OrderRepository extends JpaRepository<Order, Long > {

    @Transactional
    public void deleteById(Long id);

}
