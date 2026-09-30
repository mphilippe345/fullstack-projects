package com.superhero.superheroapi.domains.cart;


import java.math.BigDecimal;
import java.util.List;

public interface ICartService {

  List<CartItem> getCartByUserEmail(String Email);

  List<CartItem> addItemToSavedCart(String email, Long productId);

  void removeItemFromSavedCart(String email, Long productId);

//  void deleteEntireCart(String email);

  CartItem saveCartItem(CartItem cartItem);

//  void deleteCartItemsInBatch(List<Long> cartItemId);

  void deleteCartItem(Long cartItemId);

  BigDecimal getCartTotal(String email);
}
