package com.superhero.superheroapi.domains.review;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ReviewModel {

    private Long id;

    private Long productId;

    private String userEmail;

    private LocalDate reviewDate;

    private Integer rating;

    private String title;

    private String comment;

}
