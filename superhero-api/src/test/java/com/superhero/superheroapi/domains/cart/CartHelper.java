package com.superhero.superheroapi.domains.cart;

import static com.superhero.superheroapi.domains.review.ReviewHelper.VALID_EMAIL;

import com.superhero.superheroapi.domains.product.Product;
import com.superhero.superheroapi.domains.product.ProductHelper;
import com.superhero.superheroapi.domains.review.Review;
import com.superhero.superheroapi.domains.users.User;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class CartHelper {

  public static final String BASE_CART_PATH = "/api/cartItems";

  public static final Long VALID_ID = 1L;

  public static final Long INVALID_ID = 0L;

  public static final Product VALID_PRODUCT1 = ProductHelper.generateValidProductWithGenre1();

  public static final Product VALID_PRODUCT2 = ProductHelper.generateValidProductWithGenre2();

  public static final List<CartItem> CART_ITEM_LIST = new ArrayList<>();

  public static CartItem generateValidCart() {
    return new CartItem(VALID_ID, VALID_USER, CART_ITEM_LIST, VALID_CART_TOTAL);
  }  public static final User VALID_USER = new User(VALID_ID, "John", "Doe", VALID_EMAIL,
      generateValidCart(),
      new ArrayList<Review>());

  public static CartItem generateValidCartItem1() {
    return new CartItem(VALID_ID, generateValidCart(), VALID_PRODUCT1,
        1, VALID_PRODUCT1.getPrice());
  }  public static final BigDecimal VALID_CART_TOTAL = generateValidCartItem1().getCost()
      .add(generate2ValidCartItems2().getCost()).setScale(2, RoundingMode.HALF_UP);

  public static CartItem generate2ValidCartItems2() {
    return new CartItem(2L, generateValidCart(), VALID_PRODUCT2,
        2, VALID_PRODUCT2.getPrice());
  }






}
