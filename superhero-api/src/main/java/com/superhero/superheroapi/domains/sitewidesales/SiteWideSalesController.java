package com.superhero.superheroapi.domains.sitewidesales;

import static com.superhero.superheroapi.constants.Paths.BASE;
import static com.superhero.superheroapi.constants.Paths.SITEWIDE_SALES_ENDPOINT;

import com.superhero.superheroapi.domains.promo.PromoController;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(BASE + SITEWIDE_SALES_ENDPOINT)
@RequiredArgsConstructor
public class SiteWideSalesController {

  private final Logger logger = LoggerFactory.getLogger(PromoController.class);

  private final ISiteWideSalesService siteWideSalesService;

  @GetMapping
  public ResponseEntity<List<SiteWideSale>> getAllSiteWideSales() {
    logger.info("Returning all sitewide sales...");
    return new ResponseEntity<>(siteWideSalesService.getAllSiteWideSales(), HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<SiteWideSale> createSiteWideSale(
      @Valid @RequestBody SiteWideSale sitewideSale) {
    logger.info("Creating site wide sale...");
    return new ResponseEntity<>(siteWideSalesService.createSiteWideSale(sitewideSale),
        HttpStatus.CREATED);
  }

  @PutMapping("/{siteWideSaleId}")
  public ResponseEntity<SiteWideSale> updateSiteWideSaleById(
      @Valid @RequestBody SiteWideSale sitewideSale, @PathVariable Long siteWideSaleId) {
    logger.info("Updating site wide sale with id: " + siteWideSaleId);
    return new ResponseEntity<>(
        siteWideSalesService.updateSiteWideSaleById(sitewideSale, siteWideSaleId),
        HttpStatus.CREATED);
  }

  @DeleteMapping("/{siteWideSaleId}")
  public ResponseEntity<Void> deleteSiteWideSaleById(@PathVariable Long siteWideSaleId) {
    logger.info("Deleting site wide sale with id: " + siteWideSaleId);
    siteWideSalesService.deleteSiteWideSaleById(siteWideSaleId);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/{siteWideSaleId}/product/{productId}")
  public ResponseEntity<DiscountedProducts>
  assignSiteWideSale(
      @PathVariable Long siteWideSaleId,
      @PathVariable Long productId
  ) {
    logger.info("Assigning site wide sale with id: " + siteWideSaleId + " to product with id: "
        + productId);
    return new ResponseEntity<>(siteWideSalesService.assignSiteWideSale(siteWideSaleId, productId),
        HttpStatus.CREATED);
  }
}
