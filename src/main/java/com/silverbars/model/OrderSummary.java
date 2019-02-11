package com.silverbars.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Immutable
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "V_ORDER_SUMMARY")
public class OrderSummary {

    @Id
    @Column(name = "ID")//not used as entity comes from a view. Forced by Entity tag.
    private Long id;

    private Double orderQuantity;

    private Long orderPrice;

    private String buySell;

}
