package com.superhero.superheroapi.domains.cart;


import com.superhero.superheroapi.domains.product.Product;
import com.superhero.superheroapi.domains.product.ProductService;
import com.superhero.superheroapi.domains.users.User;
import com.superhero.superheroapi.domains.users.UserRepository;
import com.superhero.superheroapi.domains.users.UserService;
import com.superhero.superheroapi.exception.BadRequest;
import com.superhero.superheroapi.exception.NotFound;
import com.superhero.superheroapi.exception.ServerError;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService implements ICartService {

  private final Logger logger = LoggerFactory.getLogger(CartService.class);

  private final UserService userService;

  private final UserRepository userRepository;


  private final CartItemRepository cartItemRepository;

  private final ProductService productService;


  /**
   * Retrieves the total cartItems value for a user with the given email.
   *
   * @param email The email of the user.
   * @return The total cartItems value as a BigDecimal.
   */
  @Override
  public BigDecimal getCartTotal(String email) {
    User userInDb = userService.findByEmail(email);

    return userInDb.getCartTotal();
  }

  /**
   * Retrieves the cartItems items for a user with the given email.
   *
   * @param userEmail The email of the user.
   * @return A list of CartItem objects representing the user's cartItems items.
   */
  @Override
  public List<CartItem> getCartByUserEmail(String userEmail) {
    User userInDb = userService.findByEmail(userEmail);

    return userInDb.getCartItems();
  }

//  /**
//   * Calculates the total cost of a cartItem.
//   *
//   * @param cartItem The CartItem object for which to calculate the total cost.
//   * @return The total cost as a BigDecimal.
//   */
//  private BigDecimal totalCalculation(CartItem cartItem) {
//    return cartItem.getCartItems().stream()
//        .map(item -> item.getCost().multiply(BigDecimal.valueOf(item.getQuantity())))
//        .reduce(BigDecimal.ZERO, BigDecimal::add)
//        .setScale(2, RoundingMode.HALF_UP);
//  }

//  /**
//   * Saves a cartItems to the repository.
//   *
//   * @param newCartItem The CartItem object to be saved.
//   * @return The saved CartItem object.
//   * @throws ServerError If an error occurs during data access.
//   */
//  @Override
//  public CartItem saveCart(CartItem newCartItem) {
//    CartItem savedCartItem;
//
//    try {
//      savedCartItem = cartRepository.save(newCartItem);
//      return savedCartItem;
//    } catch (DataAccessException e) {
//      logger.error(e.getMessage());
//      throw new ServerError(e.getMessage());
//    }
//  }

  /**
   * Saves a cartItems item to the repository.
   *
   * @param newCartItem The CartItem object to be saved.
   * @return The saved CartItem object.
   * @throws ServerError If an error occurs during data access.
   */
  public CartItem saveCartItem(CartItem newCartItem) {
    CartItem savedCartItem;

    try {
      // Attempt to save the cartItems item to the repository
      savedCartItem = cartItemRepository.save(newCartItem);
      return savedCartItem;
    } catch (DataAccessException e) {
      logger.error(e.getMessage());
      throw new ServerError(e.getMessage());
    }
  }

  /**
   * Retrieves a duplicate cartItems item from the cartItems.
   *
   * @param userId   The ID of the cartItems.
   * @param productId The ID of the product.
   * @return The duplicate CartItem or null if not found.
   * @throws ServerError If an error occurs during data access.
   */
  public CartItem getDuplicateCartItem(long userId, Long productId) {
    CartItem duplicateItem;

    try {
      duplicateItem = cartItemRepository.findDuplicateItemsInCart(userId, productId);
    } catch (DataAccessException e) {
      logger.error(e.getMessage());
      throw new ServerError(e.getMessage());
    }
    return duplicateItem;
  }

  /**
   * Adds an item to the user's cartItems.
   *
   * @param userInDb   The User object for the user.
   * @param addedItem  The Product to be added to the cartItems.
   * @return The updated CartItem.
   */
  public List<CartItem> addItem(User userInDb, Product addedItem) {
    CartItem newCartItem = new CartItem();
    List<CartItem> savedCartItems = userInDb.getCartItems();

    CartItem cartItem = getDuplicateCartItem(userInDb.getId(), addedItem.getId());

    System.out.println(cartItem);

    if (cartItem != null) {
      cartItem.setQuantity(cartItem.getQuantity() + 1);
      saveCartItem(cartItem);
    } else {
      newCartItem.setProduct(addedItem);
      newCartItem.setQuantity(1);
      newCartItem.setCost(addedItem.getPrice());
      newCartItem.setUser(userInDb);
//      newCartItem.setCartItems(savedCartItem);
      saveCartItem(newCartItem);
      savedCartItems.add(newCartItem);
    }


    return savedCartItems;
  }

  /**
   * Adds an item to the saved cartItems of the user.
   *
   * @param userEmail The email of the user.
   * @param productId The ID of the product to be added.
   * @return The updated CartItem.
   * @throws ServerError If an error occurs during data access.
   */
  @Override
  @Transactional
  public List<CartItem> addItemToSavedCart(String userEmail, Long productId) {
    User userInDb;
    Product addedItem;

    addedItem = productService.getProductById(productId);

    userInDb = userService.findByEmail(userEmail);

    userInDb.setCartItems(addItem(userInDb, addedItem));

    try {
      userRepository.save(userInDb);
    } catch (DataAccessException e) {
      logger.error(e.getMessage());
      throw new ServerError(e.getMessage());
    }

    return userInDb.getCartItems();
  }

  /**
   * Removes an item from the user's cartItems.
   *
   * @param userCartItem   The CartItem object for the user.
   * @param itemInCart The Product to be removed from the cartItems.
   * @param userEmail  The email of the user.
   * @return The updated CartItem.
   */
  public List<CartItem> removeItem(List<CartItem> cartItems, Product itemInCart, String userEmail, Long userId) {

    if (cartItems.isEmpty()) {
      throw new BadRequest("User with email: " + userEmail + " has no products in the cartItem");
    }

    CartItem cartItem = getDuplicateCartItem(userId, itemInCart.getId());

    if (cartItem != null) {
      if (cartItem.getQuantity() == 1) {
        deleteCartItem(cartItem.getId());
        cartItems.remove(cartItem);
      } else {
        cartItem.setQuantity(cartItem.getQuantity() - 1);
        saveCartItem(cartItem);
      }
    }

    return cartItems;
  }

  /**
   * Removes an item from the saved cartItems of the user.
   *
   * @param userEmail The email of the user.
   * @param productId The ID of the product to be removed.
   */
  @Override
  public void removeItemFromSavedCart(String userEmail, Long productId) {
    User userInDb;
    Product itemInCart;

    userInDb = userService.findByEmail(userEmail);

    List<CartItem> cartItems =  userInDb.getCartItems();

    itemInCart = productService.getProductById(productId);

    userInDb.setCartItems(removeItem(cartItems, itemInCart, userEmail, userInDb.getId()));

    userService.save(userInDb);
  }

//  /**
//   * Deletes the entire cartItems of a user.
//   *
//   * @param userEmail The email of the user.
//   * @throws ServerError If an error occurs during data access.
//   */
//  @Override
//  public void deleteEntireCart(String userEmail) {
//    User userInDb;
//    CartItem userCartItem;
//
//    List<Long> cartItemIds = new ArrayList<>();
//
//    userInDb = userService.findByEmail(userEmail);
//
//    userCartItem = userInDb.getCartItems();
//
//    if (!userCartItem.getCartItems().isEmpty()) {
//      for (CartItem cartItem : userCartItem.getCartItems()) {
//        cartItemIds.add(cartItem.getId());
//      }
//    } else {
//      throw new BadRequest("User with email: " + userEmail + " already has an empty cartItems");
//    }
//
//    deleteCartItemsInBatch(cartItemIds);
//
//    userCartItem.getCartItems().clear();
//
//    userCartItem.setCartTotal(BigDecimal.valueOf(0.00));
//    saveCart(userCartItem);
//
//    userInDb.setCartItems(userCartItem);
//
//    userService.save(userInDb);
//  }

  /**
   * Deletes a cartItems item by its ID.
   *
   * @param cartItemId The ID of the cartItems item to be deleted.
   * @throws ServerError If an error occurs during data access.
   */
  @Override
  public void deleteCartItem(Long cartItemId) {
    CartItem cartItemToDelete;

    try {
      cartItemToDelete = cartItemRepository.findById(cartItemId).orElse(null);
    } catch (DataAccessException e) {
      logger.error(e.getMessage());
      throw new ServerError(e.getMessage());
    }

    if (cartItemToDelete == null) {
      throw new NotFound("CartItem Item with id:" + cartItemId + " could not be found");
    }

    try {
      cartItemRepository.delete(cartItemToDelete);
    } catch (DataAccessException e) {
      logger.error(e.getMessage());
      throw new ServerError(e.getMessage());
    }
  }

//  /**
//   * Deletes a list of cartItems items in batch by their IDs.
//   *
//   * @param cartItemIds The list of cartItems item IDs to be deleted.
//   * @throws ServerError If an error occurs during data access.
//   */
//  @Override
//  public void deleteCartItemsInBatch(List<Long> cartItemIds) {
//    try {
//      cartItemRepository.deleteAllByIdInBatch(cartItemIds);
//    } catch (DataAccessException e) {
//      logger.error(e.getMessage());
//      throw new ServerError(e.getMessage());
//    }
//  }

}



































