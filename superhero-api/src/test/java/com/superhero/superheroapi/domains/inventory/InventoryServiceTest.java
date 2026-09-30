package com.superhero.superheroapi.domains.inventory;

import static com.superhero.superheroapi.domains.inventory.InventoryHelper.INVALID_ID;
import static com.superhero.superheroapi.domains.inventory.InventoryHelper.NULL_CONDITION;
import static com.superhero.superheroapi.domains.inventory.InventoryHelper.VALID_AMOUNT;
import static com.superhero.superheroapi.domains.inventory.InventoryHelper.VALID_CONDITION;
import static com.superhero.superheroapi.domains.inventory.InventoryHelper.VALID_ID;
import static com.superhero.superheroapi.domains.inventory.InventoryHelper.VALID_PRODUCT;
import static com.superhero.superheroapi.domains.inventory.InventoryHelper.generateValidInventoryWithConditionAfter;
import static com.superhero.superheroapi.domains.inventory.InventoryHelper.generateValidInventoryWithConditionBefore;
import static com.superhero.superheroapi.domains.inventory.InventoryHelper.generateValidInventoryWithNoConditionAfter;
import static com.superhero.superheroapi.domains.inventory.InventoryHelper.generateValidInventoryWithNoConditionBefore;
import static com.superhero.superheroapi.domains.product.ProductHelper.generateInvalidProductWithGenre2;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.superhero.superheroapi.domains.product.Product;
import com.superhero.superheroapi.domains.product.ProductService;
import com.superhero.superheroapi.exception.NotFound;
import com.superhero.superheroapi.exception.ServerError;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;

@ExtendWith(MockitoExtension.class)
public class InventoryServiceTest {
  @InjectMocks
  private InventoryService inventoryService;

  @Mock
  private InventoryRepository inventoryRepository;

  @Mock
  private ProductService productService;

  Inventory testInventoryWithConditionBefore;
  Inventory testInventoryWithConditionAfter;
  Inventory testInventoryWithNoConditionBefore;
  Inventory testInventoryWithNoConditionAfter;

  @BeforeEach
  public void setUp() {
    testInventoryWithConditionBefore = generateValidInventoryWithConditionBefore();
    testInventoryWithConditionAfter = generateValidInventoryWithConditionAfter();

    testInventoryWithNoConditionBefore = generateValidInventoryWithNoConditionBefore();
    testInventoryWithNoConditionAfter = generateValidInventoryWithNoConditionAfter();
  }

  @Test
  public void testUpdateInventoryAmountWithCondition() {
    when(inventoryRepository.findByProductIdAndCondition(VALID_ID, VALID_CONDITION)).thenReturn(
        Optional.of(testInventoryWithConditionBefore));
    doNothing().when(productService).updateBestSellers();
    doNothing().when(productService).updateStockStatusAllProducts();
    doNothing().when(productService).updateStockStatus(any(Product.class));
    Inventory expected = testInventoryWithConditionAfter;
    Inventory actual = inventoryService.updateInventoryAmount(VALID_PRODUCT, VALID_AMOUNT,
        VALID_CONDITION);
    assertEquals(expected, actual);
  }

  @Test
  public void testUpdateInventoryAmountWithNoCondition() {
    when(inventoryRepository.findByProductIdAndCondition(VALID_ID, VALID_CONDITION)).thenReturn(
        Optional.of(testInventoryWithConditionBefore));
    doNothing().when(productService).updateBestSellers();
    doNothing().when(productService).updateStockStatusAllProducts();
    doNothing().when(productService).updateStockStatus(any(Product.class));
    Inventory expected = testInventoryWithNoConditionAfter;
    Inventory actual = inventoryService.updateInventoryAmount(VALID_PRODUCT, VALID_AMOUNT, NULL_CONDITION
    );
    assertEquals(expected, actual);
  }

  @Test
  public void testUpdateInventoryAmountThrowsNotFoundException() {
    when(inventoryRepository.findByProductIdAndCondition(INVALID_ID, VALID_CONDITION)).thenThrow(new NotFound("Simulated exception"));
    Throwable actual = assertThrows(NotFound.class,
        () -> inventoryService.updateInventoryAmount(generateInvalidProductWithGenre2(), VALID_AMOUNT, NULL_CONDITION));
    Throwable expected = new NotFound(
        "Simulated exception");
    assertEquals(expected.getMessage(), actual.getMessage(), "The message in the error thrown do not match the one that's expected.");

  }


  @Test
  public void testUpdateInventoryAmountThrowsDataAccessException() {
    when(inventoryRepository.findByProductIdAndCondition(INVALID_ID, VALID_CONDITION)).thenReturn(
        Optional.of(testInventoryWithConditionBefore));
    when(inventoryService.updateInventoryAmount(generateInvalidProductWithGenre2(), VALID_AMOUNT, NULL_CONDITION)).thenThrow(new DataAccessException("Simulated exception") {
    });
    ServerError serverError = assertThrows(ServerError.class, () -> inventoryService.updateInventoryAmount(generateInvalidProductWithGenre2(), VALID_AMOUNT, NULL_CONDITION));
    assertEquals("Simulated exception", serverError.getMessage(), "The message in the error thrown do not match the one that's expected.");

  }

}
