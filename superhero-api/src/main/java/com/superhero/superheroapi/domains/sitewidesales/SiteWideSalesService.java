package com.superhero.superheroapi.domains.sitewidesales;

import com.superhero.superheroapi.domains.product.ProductService;
import com.superhero.superheroapi.exception.Conflict;
import com.superhero.superheroapi.exception.NotFound;
import com.superhero.superheroapi.exception.ServerError;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SiteWideSalesService implements ISiteWideSalesService {

  private final Logger logger = LoggerFactory.getLogger(SiteWideSalesService.class);

  private final SiteWideSalesRepository sitewideSalesRepository;

  private final DiscountedProductsRepository discountedProductsRepository;

  private final ProductService productService;

  /**
   * Retrieves a list of all site wide sales.
   *
   * @return A list of all site wide sales.
   * @throws ServerError if there is an error accessing the data repository.
   */
  @Override
  public List<SiteWideSale> getAllSiteWideSales() {
    List<SiteWideSale> siteWideSales;
    try {
      siteWideSales = sitewideSalesRepository.findAll();
    } catch (DataAccessException e) {
      logger.error(e.getMessage());
      throw new ServerError(e.getMessage());
    }
    updateFieldsOnUse(siteWideSales);
    return siteWideSales;
  }

  /**
   * Creates a new siteWide sale.
   *
   * @param newSiteWideSale The SiteWideSale object representing the new site wide sale to be
   *                        created.
   * @return The SiteWideSale object representing the newly created site wide sale.
   * @throws ServerError if there is an error accessing the data repository.
   */
  @Override
  public SiteWideSale createSiteWideSale(SiteWideSale newSiteWideSale) {
    SiteWideSale savedSiteWideSale;
    try {
      savedSiteWideSale = sitewideSalesRepository.save(newSiteWideSale);
    } catch (DataAccessException e) {
      logger.error(e.getMessage());
      throw new ServerError(e.getMessage());
    }
    return savedSiteWideSale;
  }

  /**
   * Updates a site wide sale.
   *
   * @param newSiteWideSale The new SiteWideSale information to be passed.
   * @param siteWideSaleId  the id of the site wide sale being updated
   * @return The updated site wide sale.
   * @throws NotFound    if either product or site wide sale aren't found by ID=
   * @throws ServerError if there is an error accessing the data repository.
   */
  @Override
  public SiteWideSale updateSiteWideSaleById(SiteWideSale newSiteWideSale, Long siteWideSaleId) {
    getSiteWideSaleById(siteWideSaleId);

    SiteWideSale updatedSiteWideSale;

    try {
      updatedSiteWideSale = sitewideSalesRepository.save(newSiteWideSale);
    } catch (DataAccessException e) {
      logger.error(e.getMessage());
      throw new ServerError(e.getMessage());
    }
    return updatedSiteWideSale;
  }

  /**
   * deletes a site wide sale by its id.
   *
   * @param siteWideSaleId The id of the site wide sale to be deleted.
   * @return null.
   * @throws ServerError if there is an error accessing the data repository.
   */
  @Override
  public SiteWideSale deleteSiteWideSaleById(Long siteWideSaleId) {
    getSiteWideSaleById(siteWideSaleId);

    try {
      sitewideSalesRepository.deleteById(siteWideSaleId);
    } catch (DataAccessException e) {
      logger.error(e.getMessage());
      throw new ServerError(e.getMessage());
    }
    return null;
  }

  /**
   * Assigns a site wide sale to a product by creating a new association between them.
   *
   * @param siteWideSaleId The ID of the site wide sale.
   * @param productId      The ID of the product.
   * @return The DiscountedComics object representing the newly created association between the site
   * wide sale and the product.
   * @throws ServerError if there is an error accessing the data repository.
   * @throws NotFound    if the site wide sale or the comic is not found.
   * @throws Conflict    if the site wide sale is already assigned to the comic.
   */
  @Override
  public DiscountedProducts assignSiteWideSale(Long siteWideSaleId, Long productId) {
    getSiteWideSaleById(siteWideSaleId);
    productService.getProductById(productId);

    DiscountedProducts saleComic = new DiscountedProducts();

    saleComic.setSitewideSaleId(siteWideSaleId);
    saleComic.setProductId(productId);

    try {
      saleComic = discountedProductsRepository.save(saleComic);
    } catch (DataIntegrityViolationException e) {
      logger.error("SiteWide Sale with ID of: " + siteWideSaleId
          + "is already assigned to comic with ID of: "
          + productId);
      throw new Conflict(
          "SiteWide Sale with ID of: " + siteWideSaleId
              + " is already assigned to comic with ID of: "
              + productId);
    } catch (DataAccessException e) {
      logger.error(e.getMessage());
      throw new ServerError(e.getMessage());
    }
    return saleComic;
  }

  /**
   * finds a specific site wide sale by its id
   *
   * @param siteWideSaleId - id
   * @return site wide sale with an id of siteWideSaleId
   */
  public SiteWideSale getSiteWideSaleById(Long siteWideSaleId) {
    SiteWideSale sale;
    try {
      sale = sitewideSalesRepository.findById(siteWideSaleId).orElse(null);
    } catch (DataAccessException e) {
      logger.error(e.getMessage());
      throw new ServerError(e.getMessage());
    }

    if (sale == null) {
      logger.error("SiteWide Sale with ID of: " + siteWideSaleId + " was not found.");
      throw new NotFound("SiteWide Sale with ID of: " + siteWideSaleId + " was not found.");
    }

    return sale;
  }

  /**
   * checks the siteWideSale's start and end date to see if it should be active or inactive then
   * changes active status if needed
   *
   * @param siteWideSale - an individual site wide sale
   */
  public void updateActiveStatus(SiteWideSale siteWideSale) {
    getSiteWideSaleById(siteWideSale.getId());
    if (LocalDateTime.now().isAfter(siteWideSale.getStartDate()) && LocalDateTime.now().isBefore(
        siteWideSale.getEndDate())) {
      siteWideSale.setActive(true);
    } else {
      siteWideSale.setActive(false);
    }
  }

  /**
   * scheduled method that fires off once when the server is initiated, should contain any method
   * that needs to happen when the server initiates.
   */
  @Scheduled(fixedRate = Long.MAX_VALUE)
  @Transactional
  public void updateFieldsOnStartUp() {
    List<SiteWideSale> allSiteWideSales = getAllSiteWideSales();
    for (SiteWideSale sitewideSale : allSiteWideSales) {
      updateActiveStatus(sitewideSale);
    }
  }

  /**
   * When a method is used with this inside, this helper method is called to call
   * updateActiveStatus
   *
   * @param allSiteWideSales - all site wide sales
   */
  public void updateFieldsOnUse(List<SiteWideSale> allSiteWideSales) {
    for (SiteWideSale sitewideSale : allSiteWideSales) {
      updateActiveStatus(sitewideSale);
    }
  }

}
