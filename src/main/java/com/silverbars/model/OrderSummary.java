package com.silverbars.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "V_ORDER_SUMMARY")
public class OrderSummary {

    @Id
    private Long id;

    private Long orderQuantity;

    private Integer orderPrice;

    private String buySell;

}
