package com.superhero.superheroapi.domains.sitewidesales;

import static com.superhero.superheroapi.domains.sitewidesales.SiteWideSalesHelper.INVALID_ID;
import static com.superhero.superheroapi.domains.sitewidesales.SiteWideSalesHelper.VALID_ID;
import static com.superhero.superheroapi.domains.sitewidesales.SiteWideSalesHelper.VALID_SECOND_ID;
import static com.superhero.superheroapi.domains.sitewidesales.SiteWideSalesHelper.generateInactiveSitewideSaleStartToday;
import static com.superhero.superheroapi.domains.sitewidesales.SiteWideSalesHelper.generateInvalidActiveSitewideSale;
import static com.superhero.superheroapi.domains.sitewidesales.SiteWideSalesHelper.generateInvalidDiscountProducts;
import static com.superhero.superheroapi.domains.sitewidesales.SiteWideSalesHelper.generateValidActiveSitewideSale;
import static com.superhero.superheroapi.domains.sitewidesales.SiteWideSalesHelper.generateValidActiveSitewideSale2;
import static com.superhero.superheroapi.domains.sitewidesales.SiteWideSalesHelper.generateValidDiscountProducts;
import static com.superhero.superheroapi.domains.sitewidesales.SiteWideSalesHelper.generateValidInactiveSitewideSale;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import com.superhero.superheroapi.domains.product.ProductService;
import com.superhero.superheroapi.exception.Conflict;
import com.superhero.superheroapi.exception.NotFound;
import com.superhero.superheroapi.exception.ServerError;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;

@ExtendWith(MockitoExtension.class)
public class SiteWideSalesServiceTest {

  @InjectMocks
  private SiteWideSalesService sitewideSalesService;

  @Mock
  private SiteWideSalesRepository sitewideSalesRepository;

  @Mock
  private DiscountedProductsRepository discountedProductsRepository;

  @Mock
  private ProductService productService;

  ModelMapper mapper = new ModelMapper();

  SiteWideSale activeSiteWideSale;
  SiteWideSale activeSiteWideSale2;
  SiteWideSale inactiveSiteWideSale;
  SiteWideSale invalidActiveSiteWideSale;
  SiteWideSale inactiveSiteWideSaleStartToday;
  DiscountedProducts validDiscountedProduct;
  DiscountedProducts invalidDiscountProduct;

  @BeforeEach
  public void setUp() {
    activeSiteWideSale = generateValidActiveSitewideSale();
    activeSiteWideSale2 = generateValidActiveSitewideSale2();
    inactiveSiteWideSale = generateValidInactiveSitewideSale();
    invalidActiveSiteWideSale = generateInvalidActiveSitewideSale();
    inactiveSiteWideSaleStartToday = generateInactiveSitewideSaleStartToday();
    validDiscountedProduct = generateValidDiscountProducts();
    invalidDiscountProduct = generateInvalidDiscountProducts();
  }

  @Test
  public void getAllSiteWideSalesReturnList() {
    when(sitewideSalesRepository.findAll()).thenReturn(
        List.of(activeSiteWideSale, inactiveSiteWideSale));
    when(sitewideSalesRepository.findById(VALID_ID)).thenReturn(
        Optional.ofNullable(activeSiteWideSale));
    when(sitewideSalesRepository.findById(VALID_SECOND_ID)).thenReturn(
        Optional.ofNullable(inactiveSiteWideSale));
    List<SiteWideSale> expected = List.of(activeSiteWideSale, inactiveSiteWideSale);
    List<SiteWideSale> actual = sitewideSalesService.getAllSiteWideSales();
    assertEquals(expected, actual,
        "Test expects the actual and expected list of sitewide sales to be the same.");
  }

  @Test
  public void getAllSiteWideSalesThrowsDataAccessException() {
    when(sitewideSalesRepository.findAll())
        .thenThrow(new DataAccessException("Simulated exception") {
        });
    ServerError serverError = assertThrows(ServerError.class,
        () -> sitewideSalesService.getAllSiteWideSales());
    assertEquals("Simulated exception", serverError.getMessage(),
        "The message in the error thrown do not match the one that's expected.");
  }

  @Test
  public void postSiteWideSaleReturnNewSale() {
    when(sitewideSalesRepository.save(activeSiteWideSale)).thenReturn(activeSiteWideSale);
    SiteWideSale expected = activeSiteWideSale;
    SiteWideSale actual = sitewideSalesService.createSiteWideSale(activeSiteWideSale);
    assertEquals(expected, actual);
  }

  @Test
  public void postSiteWideSaleThrowsDataAccessException() {
    when(sitewideSalesRepository.save(invalidActiveSiteWideSale))
        .thenThrow(new DataAccessException("Simulated exception") {
        });
    ServerError serverError = assertThrows(ServerError.class,
        () -> sitewideSalesService.createSiteWideSale(invalidActiveSiteWideSale));
    assertEquals("Simulated exception", serverError.getMessage(),
        "The message in the error thrown do not match the one that's expected.");
  }

  @Test
  public void deleteSiteWideSaleByIdReturnsNoContent() {
    when(sitewideSalesRepository.findById(VALID_ID)).thenReturn(
        Optional.ofNullable(activeSiteWideSale));
    SiteWideSale expected = null;
    SiteWideSale actual = sitewideSalesService.deleteSiteWideSaleById(VALID_ID);
    assertEquals(expected, actual);
  }

  @Test
  public void deleteSiteWideSaleByIdThrowsNotFound() {
    Throwable actual = Assertions.assertThrows(NotFound.class,
        () -> sitewideSalesService.deleteSiteWideSaleById(INVALID_ID));
    Throwable expected = new NotFound("SiteWide Sale with ID of: 0 was not found.") {
    };
    assertEquals(expected.getMessage(), actual.getMessage(),
        "The message in the error thrown do not match the one that's expected.");
  }

  @Test
  public void deleteSiteWideSaleByIdThrowsServerError() {
    when(sitewideSalesRepository.findById(VALID_ID)).thenReturn(Optional.ofNullable(
        activeSiteWideSale));
    doThrow(new DataAccessException("Simulated exception") {
    }).when(sitewideSalesRepository).deleteById(VALID_ID);
    Throwable actual = Assertions.assertThrows(ServerError.class,
        () -> sitewideSalesService.deleteSiteWideSaleById(VALID_ID));
    Throwable expected = new ServerError("Simulated exception") {
    };
    assertEquals(expected.getMessage(), actual.getMessage(),
        "The message in the error thrown do not match the one that's expected.");
  }

  @Test
  public void updateSiteWideSaleByIdReturnUpdateSale() {
    when(sitewideSalesRepository.findById(VALID_ID)).thenReturn(
        Optional.ofNullable(activeSiteWideSale));
    when(sitewideSalesRepository.save(activeSiteWideSale2)).thenReturn(activeSiteWideSale2);
    SiteWideSale expected = activeSiteWideSale2;
    SiteWideSale actual = sitewideSalesService.updateSiteWideSaleById(activeSiteWideSale2,
        VALID_ID);
    assertEquals(expected, actual);
  }

  @Test
  public void updateSiteWideSaleByIdThrowsDataAccessException() {
    when(sitewideSalesRepository.findById(VALID_ID)).thenReturn(
        Optional.ofNullable(activeSiteWideSale));
    when(sitewideSalesRepository.save(activeSiteWideSale))
        .thenThrow(new DataAccessException("Simulated exception") {
        });
    ServerError serverError = assertThrows(ServerError.class,
        () -> sitewideSalesService.updateSiteWideSaleById(activeSiteWideSale, VALID_ID));
    assertEquals("Simulated exception", serverError.getMessage(),
        "The message in the error thrown do not match the one that's expected.");

  }

  @Test
  public void updateSiteWideSaleByIdThrowsNotFound() {
    Throwable actual = Assertions.assertThrows(NotFound.class,
        () -> sitewideSalesService.updateSiteWideSaleById(invalidActiveSiteWideSale, INVALID_ID));
    Throwable expected = new NotFound("SiteWide Sale with ID of: 0 was not found.") {
    };
    assertEquals(expected.getMessage(), actual.getMessage(),
        "The message in the error thrown do not match the one that's expected.");
  }

  @Test
  public void assignSiteWideSaleToProductReturnValidDiscountedProduct() {
    when(sitewideSalesRepository.findById(VALID_ID)).thenReturn(
        Optional.ofNullable(activeSiteWideSale));
    when(discountedProductsRepository.save(validDiscountedProduct)).thenReturn(
        validDiscountedProduct);
    DiscountedProducts expected = validDiscountedProduct;
    DiscountedProducts actual = sitewideSalesService.assignSiteWideSale(VALID_ID, VALID_ID);
    assertEquals(expected, actual);
  }

  @Test
  public void assignSiteWideSaleToProductThrowsDataAccessException() {
    when(sitewideSalesRepository.findById(VALID_ID)).thenReturn(
        Optional.ofNullable(activeSiteWideSale));
    when(discountedProductsRepository.save(invalidDiscountProduct))
        .thenThrow(new DataAccessException("Simulated exception") {
        });
    ServerError serverError = assertThrows(ServerError.class,
        () -> sitewideSalesService.assignSiteWideSale(VALID_ID, VALID_ID));
    assertEquals("Simulated exception", serverError.getMessage(),
        "The message in the error thrown do not match the one that's expected.");
  }

  @Test
  public void assignSiteWideSaleToProductThrowsNotFound() {
    Throwable actual = Assertions.assertThrows(NotFound.class,
        () -> sitewideSalesService.assignSiteWideSale(INVALID_ID, VALID_ID));
    Throwable expected = new NotFound("SiteWide Sale with ID of: 0 was not found.") {
    };
    assertEquals(expected.getMessage(), actual.getMessage(),
        "The message in the error thrown do not match the one that's expected.");
  }

  @Test
  public void assignSiteWideSaleThrowsDataIntegrityViolationException() {
    when(sitewideSalesRepository.findById(VALID_ID)).thenReturn(
        Optional.ofNullable(activeSiteWideSale));
    when(discountedProductsRepository.save(invalidDiscountProduct))
        .thenThrow(new DataIntegrityViolationException(
            "SiteWide Sale with ID of: " + VALID_ID + " is already assigned to comic with ID of: "
                + VALID_ID) {
        });
    Conflict conflict = assertThrows(Conflict.class,
        () -> sitewideSalesService.assignSiteWideSale(VALID_ID, VALID_ID));
    assertEquals(
        "SiteWide Sale with ID of: " + VALID_ID + " is already assigned to comic with ID of: "
            + VALID_ID, conflict.getMessage(),
        "The message in the error thrown do not match the one that's expected.");
  }

  @Test
  public void getSiteWideSaleByIdThrowsDataAccessException() {
    when(sitewideSalesRepository.findById(VALID_ID)).thenReturn(
        Optional.ofNullable(activeSiteWideSale));
    when(sitewideSalesService.getSiteWideSaleById(VALID_ID))
        .thenThrow(new DataAccessException("Simulated exception") {
        });
    ServerError serverError = assertThrows(ServerError.class,
        () -> sitewideSalesService.getSiteWideSaleById(VALID_ID));
    assertEquals("Simulated exception", serverError.getMessage(),
        "The message in the error thrown do not match the one that's expected.");
  }

  @Test
  public void updateActiveStatusTurnsActive() {
    SiteWideSale sitewideSale = new SiteWideSale();
    sitewideSale.setId(VALID_ID);
    sitewideSale.setStartDate(LocalDateTime.now().minusDays(1));
    sitewideSale.setEndDate(LocalDateTime.now().plusDays(1));

    when(sitewideSalesRepository.findById(VALID_ID)).thenReturn(Optional.of(sitewideSale));

    sitewideSalesService.updateActiveStatus(sitewideSale);

    assertEquals(true, sitewideSale.isActive(), "SiteWideSale should be active");
  }

  @Test
  public void updateActiveStatusTurnsInactive() {
    SiteWideSale sitewideSale = new SiteWideSale();
    sitewideSale.setId(VALID_ID);
    sitewideSale.setStartDate(LocalDateTime.now().minusDays(1));
    sitewideSale.setEndDate(LocalDateTime.now());

    when(sitewideSalesRepository.findById(VALID_ID)).thenReturn(Optional.of(sitewideSale));

    sitewideSalesService.updateActiveStatus(sitewideSale);

    assertEquals(false, sitewideSale.isActive(), "SiteWideSale should be inactive");
  }

  @Test
  void testUpdateFieldsOnStartUp() {
    SiteWideSale sale1 = new SiteWideSale();
    sale1.setId(1L);
    sale1.setStartDate(LocalDateTime.now().minusDays(1));
    sale1.setEndDate(LocalDateTime.now().plusDays(1));

    SiteWideSale sale2 = new SiteWideSale();
    sale2.setId(2L);
    sale2.setStartDate(LocalDateTime.now().minusDays(2));
    sale2.setEndDate(LocalDateTime.now().plusDays(2));

    List<SiteWideSale> allSiteWideSales = Arrays.asList(sale1, sale2);

    when(sitewideSalesRepository.findAll()).thenReturn(allSiteWideSales);
    when(sitewideSalesRepository.findById(1L)).thenReturn(Optional.of(sale1));
    when(sitewideSalesRepository.findById(2L)).thenReturn(Optional.of(sale2));

    sitewideSalesService.updateFieldsOnStartUp();

    assertEquals(true, sale1.isActive(), "sale1 should be active");
    assertEquals(true, sale2.isActive(), "sale2 should be active");

  }

}
