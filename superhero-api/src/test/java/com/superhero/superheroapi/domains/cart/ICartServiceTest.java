package com.superhero.superheroapi.domains.cart;

import static com.superhero.superheroapi.domains.cart.CartHelper.VALID_ID;
import static com.superhero.superheroapi.domains.cart.CartHelper.VALID_PRODUCT1;
import static com.superhero.superheroapi.domains.cart.CartHelper.VALID_PRODUCT2;
import static com.superhero.superheroapi.domains.cart.CartHelper.VALID_USER;
import static com.superhero.superheroapi.domains.cart.CartHelper.generate2ValidCartItems2;
import static com.superhero.superheroapi.domains.cart.CartHelper.generateValidCart;
import static com.superhero.superheroapi.domains.cart.CartHelper.generateValidCartItem1;
import static com.superhero.superheroapi.domains.review.ReviewHelper.VALID_EMAIL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.superhero.superheroapi.domains.product.Product;
import com.superhero.superheroapi.domains.product.ProductRepository;
import com.superhero.superheroapi.domains.product.ProductService;
import com.superhero.superheroapi.domains.users.User;
import com.superhero.superheroapi.domains.users.UserRepository;
import com.superhero.superheroapi.domains.users.UserService;
import com.superhero.superheroapi.exception.BadRequest;
import com.superhero.superheroapi.exception.ServerError;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;

@ExtendWith(MockitoExtension.class)
public class ICartServiceTest {

  @InjectMocks
  CartService cartServiceImpl;
  @Mock
  CartRepository cartRepository;

  @Mock
  ProductRepository productRepository;

  @Mock
  UserService userService;

  @Mock
  ProductService productService;
  CartItem testCartItem;

  User testUser = new User();

  User testUserWithEmptyCart = new User();
  CartItem testCartItem;
  CartItem testCartItem2;
  Product testProduct;
  Product testProduct2;
  List<CartItem> cartItems = new ArrayList<>();
  @Mock
  private UserRepository userRepository;
  @Mock
  private CartItemRepository cartItemRepository;

  @BeforeEach
  public void setup() {
    testUser = VALID_USER;

    testUserWithEmptyCart.setId(2L);
    testUserWithEmptyCart.setEmail("email@email1.com");

    testProduct = VALID_PRODUCT1;

    testProduct2 = VALID_PRODUCT2;

    testCartItem = generateValidCart();

    testCartItem = generateValidCartItem1();
    testCartItem2 = generate2ValidCartItems2();
    cartItems.addAll(Arrays.asList(testCartItem, testCartItem2));

    testCartItem.setCartItems(cartItems);
  }

  @Test
  public void getCartTotalReturnsTotal() {
    when(userService.findByEmail(VALID_EMAIL)).thenReturn(testUser);

    BigDecimal expected = testCartItem.getCost().add(testCartItem2.getCost())
        .setScale(2, RoundingMode.HALF_UP);

    BigDecimal actual = cartServiceImpl.getCartTotal(VALID_EMAIL);

    assertEquals(expected, actual,
        "The message in the error thrown do not match the one that's expected.");

  }

  @Test
  public void getDuplicateCartItemReturnsDataAccessException() {
    when(cartRepository.findDuplicateItemsInCart(any(Long.class), any(Long.class))).thenThrow(
        new DataAccessException("Simulated exception") {
        });
    ServerError serverError = assertThrows(ServerError.class,
        () -> cartServiceImpl.getDuplicateCartItem(VALID_ID, VALID_ID));
    assertEquals("Simulated exception", serverError.getMessage(),
        "The message in the error thrown do not match the one that's expected.");
  }

  @Test
  public void addItemToSavedCartReturnsCart() {
    when(cartRepository.save(any(CartItem.class))).thenReturn(testCartItem);
    when(userService.findByEmail(VALID_EMAIL)).thenReturn(testUser);
    when(productService.getProductById(VALID_ID)).thenReturn(testProduct);
    CartItem expected = testCartItem;
    CartItem actual = cartServiceImpl.addItemToSavedCart(VALID_EMAIL, VALID_ID);
    assertEquals(expected, actual, "The CartItem objects do not match.");
  }

  @Test
  public void addItemToSavedCartThrowsDataAccessException() {
    when(userService.findByEmail(VALID_EMAIL)).thenReturn(testUser);
    when(productService.getProductById(VALID_ID)).thenReturn(testProduct);
    when(userRepository.save(any(User.class))).thenThrow(
        new DataAccessException("Simulated exception") {
        });
    ServerError serverError = assertThrows(ServerError.class,
        () -> cartServiceImpl.addItemToSavedCart(VALID_EMAIL, VALID_ID));
    assertEquals("Simulated exception", serverError.getMessage(),
        "The message in the error thrown do not match the one that's expected.");
  }

  @Test
  public void addItemToSavedCartIncrementsQuantity() {
    when(cartRepository.save(any(CartItem.class))).thenReturn(testCartItem);
    when(userService.findByEmail(VALID_EMAIL)).thenReturn(testUser);
    when(productService.getProductById(VALID_ID)).thenReturn(testProduct);
    when(cartRepository.findDuplicateItemsInCart(VALID_ID, VALID_ID)).thenReturn(testCartItem);
    when(cartItemRepository.save(any(CartItem.class))).thenAnswer(
        AdditionalAnswers.returnsFirstArg());

    int expected = testCartItem.getCartItems().get(0).getQuantity() + 1;
    int actual = cartServiceImpl.addItemToSavedCart(VALID_EMAIL, VALID_ID)
        .getCartItems()
        .get(0)
        .getQuantity();
    assertEquals(expected, actual, "The CartItem Item's Quantity does not match");
    verify(cartItemRepository).save(testCartItem);
  }


  @Test
  public void removeItemDecrementsQuantity() {
    when(cartRepository.save(any(CartItem.class))).thenReturn(testCartItem);
    when(cartRepository.findDuplicateItemsInCart(VALID_ID, VALID_ID)).thenReturn(testCartItem2);
    when(cartItemRepository.save(any(CartItem.class))).thenAnswer(
        AdditionalAnswers.returnsFirstArg());

    int expected = 1;
    int actual = cartServiceImpl.removeItem(testCartItem, testProduct2, VALID_EMAIL)
        .getCartItems()
        .get(1)
        .getQuantity();
    assertEquals(expected, actual, "The CartItem objects do not match.");
    verify(cartItemRepository).save(testCartItem2);
  }

  @Test
  public void removeItemRemovesAnItemFromUsersCart() {
    when(cartRepository.save(any(CartItem.class))).thenReturn(testCartItem);
    when(cartRepository.findDuplicateItemsInCart(VALID_ID, VALID_ID)).thenReturn(testCartItem);
    when(cartItemRepository.findById(VALID_ID)).thenReturn(Optional.ofNullable(testCartItem));

    List<CartItem> expected = testCartItem.getCartItems();
    expected.remove(testCartItem);

    List<CartItem> actual = cartServiceImpl.removeItem(testCartItem, testProduct, VALID_EMAIL)
        .getCartItems();
    assertEquals(expected, actual, "The CartItem objects do not match.");
  }

  @Test
  public void removeItemThrows400EmptyCart() {
    testCartItem.getCartItems().removeAll(cartItems);
    Throwable actual = assertThrows(BadRequest.class,
        () -> cartServiceImpl.removeItem(testCartItem, testProduct, VALID_EMAIL));
    Throwable expected = new BadRequest(
        "User with email: email@email.com has no products in the cartItems");
    assertEquals(expected.getMessage(), actual.getMessage(),
        "The message in the error thrown do not match the one that's expected.");
  }

  @Test
  public void removeItemFromSavedCartSavesUserCart() {
    testUser.setCartItems(testCartItem);
    when(cartRepository.save(any(CartItem.class))).thenReturn(testCartItem);
    when(cartRepository.findDuplicateItemsInCart(VALID_ID, VALID_ID)).thenReturn(testCartItem);
    when(cartItemRepository.findById(VALID_ID)).thenReturn(Optional.ofNullable(testCartItem));
    when(productService.getProductById(VALID_ID)).thenReturn(testProduct);
    when(userService.findByEmail(VALID_EMAIL)).thenReturn(testUser);

    List<CartItem> expected = testCartItem.getCartItems();
    expected.remove(testCartItem);

    cartServiceImpl.removeItemFromSavedCart(VALID_EMAIL, VALID_ID);

    List<CartItem> actual = cartServiceImpl.getCartByUserEmail(VALID_EMAIL);

    assertEquals(expected, actual, "The CartItem objects do not match.");
  }

  @Test
  public void deleteEntireCartRemovesAllItems() {
    testUser.setCartItems(testCartItem);
    when(userService.findByEmail(VALID_EMAIL)).thenReturn(testUser);

    List<CartItem> expected = new ArrayList<>();

    cartServiceImpl.deleteEntireCart(VALID_EMAIL);

    List<CartItem> actual = cartServiceImpl.getCartByUserEmail(VALID_EMAIL);

    assertEquals(expected, actual, "The CartItem objects do not match.");

  }

  @Test
  public void deleteEntireCartThrows400EmptyCart() {
    testUser.setCartItems(testCartItem);
    testCartItem.getCartItems().removeAll(cartItems);
    when(userService.findByEmail(VALID_EMAIL)).thenReturn(testUser);

    Throwable actual = assertThrows(BadRequest.class,
        () -> cartServiceImpl.deleteEntireCart(VALID_EMAIL));
    Throwable expected = new BadRequest(
        "User with email: email@email.com already has an empty cartItems");
    assertEquals(expected.getMessage(), actual.getMessage(),
        "The message in the error thrown do not match the one that's expected.");
  }


}
