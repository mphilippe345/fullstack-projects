package com.superhero.superheroapi.domains.purchase;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class PurchaseDto {

  private String email;
  private String firstName;
  private String orderNumber;
  private BigDecimal orderTotal;
  private LocalDate date;
  private OrderStatus orderStatus;
}
