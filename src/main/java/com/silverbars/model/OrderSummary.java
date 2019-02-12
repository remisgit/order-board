package com.silverbars.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Immutable
@Getter
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "V_ORDER_SUMMARY")
public class OrderSummary {

    @Id
    @Column(name = "ID")//not used as entity comes from a view but Entity tag forces to have an id field.
    @JsonIgnore
    private Long id;

    private Double orderQuantity;

    private Long orderPrice;

    private String buySell;

}
