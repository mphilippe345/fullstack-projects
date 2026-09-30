package com.superhero.superheroapi.domains.inventory;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class InventoryModel {
  private Long id;
  private String condition;
  private Integer amount;
  private BigDecimal rate;
}
