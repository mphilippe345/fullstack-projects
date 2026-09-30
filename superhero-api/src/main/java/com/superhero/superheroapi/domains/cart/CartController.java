package com.superhero.superheroapi.domains.cart;


import java.math.BigDecimal;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cartItem")
@RequiredArgsConstructor
public class CartController {

  private final Logger logger = LoggerFactory.getLogger(CartController.class);

  private final ICartService ICartService;

  @GetMapping("/{email}")
  public ResponseEntity<List<CartItem>> getCartByUserEmail(@PathVariable String email) {
    logger.info("Returning user's cartItems...");
    return new ResponseEntity<>(ICartService.getCartByUserEmail(email), HttpStatus.OK);
  }

  @GetMapping("/{email}/total")
  public ResponseEntity<BigDecimal> getCartTotal(@PathVariable String email) {
    logger.info("Returning user's cartItems total...");
    return new ResponseEntity<>(ICartService.getCartTotal(email), HttpStatus.OK);
  }

  @PostMapping("/{email}/{productId}")
  public ResponseEntity<List<CartItem>> addItemToSavedCart(@PathVariable String email,
      @PathVariable Long productId) {
    logger.info("Adding Item to saved cartItems..");
    return new ResponseEntity<>(ICartService.addItemToSavedCart(email, productId),
        HttpStatus.CREATED);
  }

  @DeleteMapping("/{email}/{productId}")
  public ResponseEntity<Void> removeItemFromSavedCart(@PathVariable String email,
      @PathVariable Long productId) {
    logger.info("Removing product from cartItems");
    ICartService.removeItemFromSavedCart(email, productId);
    return ResponseEntity.noContent().build();
  }

//  @DeleteMapping("/{email}")
//  public ResponseEntity<Void> deleteEntireCart(@PathVariable String email) {
//    logger.info("Emptying user's cartItems... ");
//    ICartService.deleteEntireCart(email);
//    return ResponseEntity.noContent().build();
//  }

}
