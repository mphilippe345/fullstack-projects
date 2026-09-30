package com.superhero.superheroapi.domains.promo;

import static com.superhero.superheroapi.constants.Paths.BASE;
import static com.superhero.superheroapi.constants.Paths.PROMO_ENDPOINT;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(BASE + PROMO_ENDPOINT)
@RequiredArgsConstructor
public class PromoController {

  private final Logger logger = LoggerFactory.getLogger(PromoController.class);

  private final IPromoService promoService;

  @GetMapping
  public ResponseEntity<List<PromoCode>> getAllPromos() {
    logger.info("Returning all promos...");
    return new ResponseEntity<>(promoService.getAllPromos(), HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<PromoCode> createPromo(@Valid @RequestBody PromoCode promo) {
    logger.info("Creating promos...");
    return new ResponseEntity<>(promoService.createPromo(promo), HttpStatus.CREATED);
  }

  @PostMapping("/{promoId}/product/{comicId}")
  public ResponseEntity<DiscountedComics>
  assignPromo(
      @PathVariable Long promoId,
      @PathVariable Long comicId
  ) {
    logger.info("Assigning promo with id: " + promoId + " to comic with id: " + comicId);
    return new ResponseEntity<>(promoService.assignPromo(promoId, comicId), HttpStatus.CREATED);
  }

  @GetMapping("/product/{comicId}")
  public ResponseEntity<List<PromoCode>>
  findAssignedPromosByProductId(
      @PathVariable Long comicId
  ) {
    logger.info("Returning All discountedComics");
    return new ResponseEntity<>(promoService.findAssignedPromosByProductId(comicId), HttpStatus.OK);
  }

}
