package com.superhero.superheroapi.domains.product;

import lombok.Data;

import java.util.List;

@Data
public class FilterFields {

    List<String> authors;
    List<String> publishers;
    List<String> genres;
}
