package com.superhero.superheroapi.domains.inventory;

import com.superhero.superheroapi.domains.product.Product;
import com.superhero.superheroapi.domains.product.ProductService;
import com.superhero.superheroapi.exception.NotFound;
import com.superhero.superheroapi.exception.ServerError;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryService implements IInventoryService{

    private final InventoryRepository inventoryRepository;

    private final ProductService productService;

    private final ModelMapper mapper = new ModelMapper();

    private final Logger logger = LoggerFactory.getLogger(InventoryService.class);

  /**
   * method that gets all inventory
   * @return
   */
  @Override
    public List<Inventory> getAllInventory() {
        try {
            return inventoryRepository.findAll();
        } catch (DataAccessException e) {
            logger.error(e.getMessage());
            throw new ServerError(e.getMessage());
        }
    }

  /**
   * method that gets all inventory of a specific product
   * @param productId - specific product
   * @return all inventory of product
   */
  @Override
    public List<Inventory> getInventoryByProductId(Long productId) {
        try {
            return inventoryRepository.findAllByProductId(productId);
        } catch (DataAccessException e) {
            logger.error(e.getMessage());
            throw new ServerError(e.getMessage());
        }
    }

    /**
     * method that updates the amount of a given product based off quantity purchased as well as its stock status and best sellers boolean if needed
     * @param product - product being purchased
     * @param quantity - amount of said product
     * @param condition - which condition of product being purchased
     * @return
     */
   @Override
    public Inventory updateInventoryAmount(Product product, int quantity, String condition) {
        Optional<Inventory> inventory = inventoryRepository.findByProductIdAndCondition(
        product.getId(), condition == null ? "new" : condition);
        if ( inventory.isEmpty() ) {
          logger.error("Inventory with productID of: " + product.getId() + " was not found.");
          throw new NotFound("Inventory with productID of: " + product.getId() + " was not found.");
        }
        Inventory inventory1 = inventory.get();
        int inventoryCount = inventory1.getAmount();
        int newAmount = inventoryCount - quantity;
       inventory1.setAmount(newAmount);
        try {
          productService.updateStockStatus(product);
          productService.updateStockStatusAllProducts();
          productService.updateBestSellers();
          return inventory1;
        } catch (DataAccessException e) {
            logger.error(e.getMessage());
            throw new ServerError(e.getMessage());
        }
    }
}
