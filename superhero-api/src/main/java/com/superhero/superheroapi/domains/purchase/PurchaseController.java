package com.superhero.superheroapi.domains.purchase;

import static com.superhero.superheroapi.constants.Paths.BASE;
import static com.superhero.superheroapi.constants.Paths.PURCHASE_ENDPOINT;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(BASE + PURCHASE_ENDPOINT)
@RequiredArgsConstructor
public class PurchaseController {

  private final Logger logger = LoggerFactory.getLogger(PurchaseController.class);

  private final IPurchaseService purchaseService;

  @GetMapping
  public ResponseEntity<List<Purchase>> findAllPurchases() {
    logger.info("Returning all purchases...");
    return new ResponseEntity<>(purchaseService.findAllPurchases(), HttpStatus.OK);
  }

  @GetMapping("/{email}")
  public ResponseEntity<Page<PurchaseDto>> getPurchasesByEmail(@PathVariable String email, @RequestParam int page, @RequestParam int size) {
    logger.info("Returning Purchases by Email...");
    return new ResponseEntity<>(purchaseService.getPurchasesByEmail(email, page, size), HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<PurchaseDto> savePurchase(@Valid @RequestBody Purchase purchase) {
    logger.info("Saving new purchase...");
    return new ResponseEntity<>(purchaseService.savePurchase(purchase), HttpStatus.CREATED);
  }

}
