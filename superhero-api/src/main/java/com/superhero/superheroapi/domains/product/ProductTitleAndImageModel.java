package com.superhero.superheroapi.domains.product;

import lombok.Data;

@Data
public class ProductTitleAndImageModel {

    private Long id;
    private String author;
    private String title;
    private String publisher;
    private String imageUrl;
    private String sku;

}
