package com.superhero.superheroapi.domains.purchase;

import com.superhero.superheroapi.domains.product.ProductModel;

import java.util.List;

public interface ISalesService {
    List<ProductModel> getTop10();
}
