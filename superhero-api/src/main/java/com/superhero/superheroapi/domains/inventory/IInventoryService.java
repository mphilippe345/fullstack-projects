package com.superhero.superheroapi.domains.inventory;

import com.superhero.superheroapi.domains.product.Product;
import java.util.List;

public interface IInventoryService {
    List<Inventory> getAllInventory();

    List<Inventory> getInventoryByProductId(Long productId);

    Inventory updateInventoryAmount(Product product, int quantity, String condition);
}
