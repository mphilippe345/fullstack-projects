package com.superhero.superheroapi.models;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class ProductModel {

  private Long id;

  private String author;

  private String title;

//  private int issue;
//
//  private String volume;

//  private String publisher;
//
//  private LocalDate releaseDate;
//
//  private LocalDate inventoryDate;
//
//  private String condition;

  private BigDecimal price;

//  private String description;

//  private String genre;

  private String imageUrl;

//  private boolean stockStatus;
//
//  private String inventory;
//
//  private String sku;
}
