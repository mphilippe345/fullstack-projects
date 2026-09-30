package com.superhero.superheroapi.domains.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class ProductStatusesModel {

    private boolean active;
    private boolean stockStatus;

}
