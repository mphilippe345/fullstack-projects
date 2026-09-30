package com.superhero.superheroapi.domains.purchase;

import static com.superhero.superheroapi.domains.product.ProductHelper.generateValidProductWithGenre1;
import static com.superhero.superheroapi.domains.purchase.PurchaseHelper.INVALID_EMAIL;
import static com.superhero.superheroapi.domains.purchase.PurchaseHelper.PAGE_NUMBER;
import static com.superhero.superheroapi.domains.purchase.PurchaseHelper.PAGE_SIZE;
import static com.superhero.superheroapi.domains.purchase.PurchaseHelper.VALID_EMAIL;
import static com.superhero.superheroapi.domains.purchase.PurchaseHelper.VALID_FIRST_NAME;
import static com.superhero.superheroapi.domains.purchase.PurchaseHelper.VALID_ID;
import static com.superhero.superheroapi.domains.purchase.PurchaseHelper.VALID_ORDER_NUMBER;
import static com.superhero.superheroapi.domains.purchase.PurchaseHelper.VALID_ORDER_TOTAL;
import static com.superhero.superheroapi.domains.purchase.PurchaseHelper.generateInvalidPurchase;
import static com.superhero.superheroapi.domains.purchase.PurchaseHelper.generateInvalidPurchaseNullLineItems;
import static com.superhero.superheroapi.domains.purchase.PurchaseHelper.generateNullPurchase;
import static com.superhero.superheroapi.domains.purchase.PurchaseHelper.generatePurchaseNullTotal;
import static com.superhero.superheroapi.domains.purchase.PurchaseHelper.generateValidPersonalInfo;
import static com.superhero.superheroapi.domains.purchase.PurchaseHelper.generateValidPurchase;
import static com.superhero.superheroapi.domains.purchase.PurchaseHelper.generateValidPurchaseResponse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import com.superhero.superheroapi.domains.cart.CartService;
import com.superhero.superheroapi.domains.email.EmailService;
import com.superhero.superheroapi.domains.inventory.InventoryService;
import com.superhero.superheroapi.domains.product.Product;
import com.superhero.superheroapi.domains.product.ProductService;
import com.superhero.superheroapi.domains.users.UserService;
import com.superhero.superheroapi.exception.BadRequest;
import com.superhero.superheroapi.exception.NotFound;
import com.superhero.superheroapi.exception.ServerError;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@ExtendWith(MockitoExtension.class)
public class PurchaseServiceTest {

  private PurchaseService purchaseService;

  @Mock
  private PurchaseRepository purchaseRepository;

  @Mock
  private ProductService productService;

  @Mock
  private LineItemRepository lineItemRepository;

  @Mock
  private InventoryService inventoryService;

  @Mock
  private EmailService emailService;

  @Mock
  private UserService userService;

  @Mock
  private CartService cartService;

  private final ModelMapper mapper = new ModelMapper();

  Purchase validPurchase;

  PurchaseDto purchaseResponse;
  Purchase invalidPurchaseNullTotal;
  Purchase invalidPurchase;
  Purchase invalidPurchaseNullLineItems;

  Product testProduct;
  Purchase testPurchase;
  Purchase testPurchase2;

  PurchaseDto testPurchaseDto;

  PurchaseDto testPurchaseDto2;
  Pageable pageable = Pageable.ofSize(PAGE_SIZE);
  Page<Purchase> purchasePage = Page.empty(pageable);
  Page<PurchaseDto> purchaseResponsePage = Page.empty(pageable);


  @BeforeEach
  public void setUp() {
    purchaseService = new PurchaseService(emailService, mapper, purchaseRepository, productService, inventoryService, lineItemRepository, userService, cartService);

    validPurchase = generateValidPurchase();
    purchaseResponse = generateValidPurchaseResponse(validPurchase);
    invalidPurchaseNullTotal = generatePurchaseNullTotal();
    invalidPurchase = generateInvalidPurchase();
    invalidPurchaseNullLineItems = generateInvalidPurchaseNullLineItems();

    testPurchase = generateNullPurchase();
    testPurchase2 = generateNullPurchase();
    testPurchase.setPersonalInfo(generateValidPersonalInfo());
    testPurchase2.setPersonalInfo(generateValidPersonalInfo());

    purchasePage = new PageImpl<>(Arrays.asList(testPurchase, testPurchase2), pageable, 2);
    testPurchaseDto = new PurchaseDto();
    testPurchaseDto.setFirstName(VALID_FIRST_NAME);
    testPurchaseDto.setEmail(VALID_EMAIL);

    testPurchaseDto2 = new PurchaseDto();
    testPurchaseDto2.setFirstName(VALID_FIRST_NAME);
    testPurchaseDto2.setEmail(VALID_EMAIL);
    purchaseResponsePage = new PageImpl<>(Arrays.asList(testPurchaseDto, testPurchaseDto2), pageable, 2);

    testProduct = generateValidProductWithGenre1();


    lenient().doNothing().when(emailService).sendPurchaseConfirmationEmail(VALID_EMAIL, validPurchase, VALID_ORDER_TOTAL);
  }

  @Test
  public void testSavePurchaseValidPurchase() {
    when(purchaseRepository.save(validPurchase)).thenReturn(validPurchase);
    when(productService.getProductById(VALID_ID)).thenReturn(generateValidProductWithGenre1());
    PurchaseDto expected = purchaseResponse;
    PurchaseDto actual = purchaseService.savePurchase(validPurchase);
    expected.setOrderNumber(VALID_ORDER_NUMBER);
    actual.setOrderNumber(VALID_ORDER_NUMBER);
    assertEquals(expected, actual);
  }

  @Test
  public void testSavePurchaseInvalidPurchaseNullTotal() {
    Throwable actual = assertThrows(BadRequest.class,
        () -> purchaseService.savePurchase(invalidPurchaseNullTotal));
    Throwable expected = new BadRequest(
        "Order total must be provided.");
    assertEquals(expected.getMessage(), actual.getMessage(), "The message in the error thrown do not match the one that's expected.");
  }

  @Test
  public void testSavePurchaseThrowsDataAccessException() {
    when(purchaseRepository.save(invalidPurchase)).thenThrow(new DataAccessException("Simulated exception") {
    });
    ServerError serverError = assertThrows(ServerError.class, () -> purchaseService.savePurchase(invalidPurchase));
    assertEquals("Simulated exception", serverError.getMessage(), "The message in the error thrown do not match the one that's expected.");
  }

  @Test
  public void testSavePurchaseThrowsBadRequestNullLineItems() {
    Throwable actual = assertThrows(BadRequest.class,
        () -> purchaseService.savePurchase(invalidPurchaseNullLineItems));
    Throwable expected = new BadRequest(
        "Line items required");
    assertEquals(expected.getMessage(), actual.getMessage(), "The message in the error thrown do not match the one that's expected.");
  }

  @Test
  public void getPurchaseByEmailReturnsPurchase() {
    when(purchaseRepository.findByEmail(VALID_EMAIL, PageRequest.of(PAGE_NUMBER, PAGE_SIZE, Sort.by("date").descending()))).thenReturn(purchasePage);
    Page<PurchaseDto> actual = purchaseService.getPurchasesByEmail(VALID_EMAIL, PAGE_NUMBER, PAGE_SIZE);
    Page<PurchaseDto> expected = purchaseResponsePage;

    assertEquals(expected, actual,
        "Test expects the actual and expected list of purchase to be the same.");
  }

  @Test
  public void testGetPurchaseByEmailWithNonExistentEmailThrowsException() {
    when(purchaseRepository.findByEmail(INVALID_EMAIL, PageRequest.of(PAGE_NUMBER, PAGE_SIZE, Sort.by("date").descending()))).thenThrow(
        new NotFound("Purchases with an email of invalid@email.com were not found."));
    Throwable actual = assertThrows(NotFound.class,
        () -> purchaseService.getPurchasesByEmail(INVALID_EMAIL, PAGE_NUMBER, PAGE_SIZE));
    Throwable expected = new NotFound(
        "Purchases with an email of invalid@email.com were not found.");
    assertEquals(expected.getMessage(), actual.getMessage(),
        "The message in the error thrown do not match the one that's expected.");
  }

  @Test
  public void getPurchaseByEmailThrowsDataAccessException() {
    when(purchaseRepository.findByEmail(VALID_EMAIL, PageRequest.of(PAGE_NUMBER, PAGE_SIZE,
        Sort.by("date").descending())))
        .thenThrow(new DataAccessException("Simulated exception") {
        });
    ServerError serverError = assertThrows(ServerError.class,
        () -> purchaseService.getPurchasesByEmail(VALID_EMAIL, PAGE_NUMBER, PAGE_SIZE));
    assertEquals("Simulated exception", serverError.getMessage(),
        "The message in the error thrown do not match the one that's expected.");
  }


}
