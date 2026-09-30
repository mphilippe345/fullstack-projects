package com.superhero.superheroapi.domains.product;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductModel {

  private Long id;
  private String title;
  private String author;
  private String description;
  private String genre;
  private String imageUrl;
  private Integer issue;
  private String volume;
  private boolean newrelease;
  private String publisher;
  private boolean stockStatus;
  private boolean active;
  private String sku;
  private LocalDate releaseDate;
  private BigDecimal price;
  private boolean bestSeller;


}
