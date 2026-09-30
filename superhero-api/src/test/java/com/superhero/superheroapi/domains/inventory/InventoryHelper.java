package com.superhero.superheroapi.domains.inventory;

import static com.superhero.superheroapi.domains.product.ProductHelper.generateValidProductWithGenre1;

import com.superhero.superheroapi.domains.product.Product;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.modelmapper.ModelMapper;

public class InventoryHelper {
  public static final String BASE_INVENTORY_PATH = "/api/inventory";
  public static final Long VALID_ID = 1L;
  public static final Long INVALID_ID = 0L;
  public static final Product VALID_PRODUCT = generateValidProductWithGenre1();
  public static int VALID_AMOUNT = 1;
  public static int VALID_AMOUNT_AFTER = 0;
  public static BigDecimal VALID_RATE = new BigDecimal("0.95");
  public static String VALID_CONDITION = "new";
  public static String NULL_CONDITION = null;
  public static final LocalDate VALID_DATE = LocalDate.now();

  public static Inventory generateValidInventoryWithConditionBefore() {
    return new Inventory(VALID_ID, VALID_PRODUCT, VALID_CONDITION, VALID_AMOUNT, VALID_DATE, VALID_RATE);
  }

  public static Inventory generateValidInventoryWithConditionAfter() {
    return new Inventory(VALID_ID, VALID_PRODUCT, VALID_CONDITION, VALID_AMOUNT_AFTER, VALID_DATE, VALID_RATE);
  }

  public static Inventory generateValidInventoryWithNoConditionBefore() {
    return new Inventory(VALID_ID, VALID_PRODUCT, NULL_CONDITION, VALID_AMOUNT, VALID_DATE, VALID_RATE);
  }

  public static Inventory generateValidInventoryWithNoConditionAfter() {
    return new Inventory(VALID_ID, VALID_PRODUCT, "new", VALID_AMOUNT_AFTER, VALID_DATE, VALID_RATE);
  }

  public static InventoryModel mapInventory(Inventory inventory) {
    ModelMapper mapper = new ModelMapper();
    return mapper.map(inventory, InventoryModel.class);
  }

}
