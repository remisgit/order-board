package com.silverbars.repository;

import com.silverbars.model.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<OrderEntity, Long > {

   /* @Query("SELECT u FROM UserEntity u WHERE LOWER(u.firstName) = LOWER(:name)")
    UserEntity retrieveByFirstName(@Param("name") String name);*/

}
