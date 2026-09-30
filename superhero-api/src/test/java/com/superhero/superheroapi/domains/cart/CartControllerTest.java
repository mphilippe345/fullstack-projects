package com.superhero.superheroapi.domains.cart;

import static com.superhero.superheroapi.domains.cart.CartHelper.BASE_CART_PATH;
import static com.superhero.superheroapi.domains.cart.CartHelper.VALID_ID;
import static com.superhero.superheroapi.domains.cart.CartHelper.VALID_USER;
import static com.superhero.superheroapi.domains.cart.CartHelper.generateValidCart;
import static com.superhero.superheroapi.domains.cart.CartHelper.generateValidCartItem1;
import static com.superhero.superheroapi.domains.review.ReviewHelper.INVALID_EMAIL;
import static com.superhero.superheroapi.domains.review.ReviewHelper.VALID_EMAIL;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.superhero.superheroapi.domains.product.Product;
import com.superhero.superheroapi.domains.product.ProductHelper;
import com.superhero.superheroapi.domains.product.ProductRepository;
import com.superhero.superheroapi.domains.users.User;
import com.superhero.superheroapi.domains.users.UserRepository;
import com.superhero.superheroapi.domains.users.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@ActiveProfiles("test")
public class CartControllerTest {

  private static MockMvc mockMvc;

  @Autowired
  CartRepository cartRepository;

  @Autowired
  ProductRepository productRepository;

  @Autowired
  UserService userService;
  CartItem testCartItem = generateValidCart();
  User testUser = new User();

  User testUserWithEmptyCart = new User();

  CartItem testCartItem = generateValidCartItem1();

  Product testProduct = ProductHelper.generateValidProductWithGenre1();

  @Autowired
  private WebApplicationContext wac;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private CartItemRepository cartItemRepository;

  @BeforeEach
  public void setup() {
    mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    testUser = userRepository.save(VALID_USER);
    testUserWithEmptyCart.setId(2L);
    testUserWithEmptyCart.setEmail("email@email1.com");
    testUserWithEmptyCart = userService.save(testUserWithEmptyCart);

    testProduct = productRepository.save(testProduct);

    testCartItem = cartRepository.save(testCartItem);

    testCartItem = cartItemRepository.save(testCartItem);
  }

  @Test
  public void getCartByUserEmailReturns200() throws Exception {
    mockMvc.perform(get(BASE_CART_PATH + "/" + VALID_EMAIL))
        .andExpect(status().isOk());
  }

  @Test
  public void getCartTotalReturns200() throws Exception {
    mockMvc.perform(get(BASE_CART_PATH + "/" + VALID_EMAIL + "/total"))
        .andExpect(status().isOk());
  }

  @Test
  public void getCartByUserEmailReturns404InvalidEmail() throws Exception {
    mockMvc.perform(get(BASE_CART_PATH + "/" + INVALID_EMAIL))
        .andExpect(status().isNotFound());
  }

  @Test
  public void addItemToSavedCartReturns201() throws Exception {
    mockMvc.perform(post(BASE_CART_PATH + "/" + VALID_EMAIL + "/" + VALID_ID))
        .andExpect(status().isCreated());
  }

  @Test
  public void addItemToSavedCartReturns404InvalidEmail() throws Exception {
    mockMvc.perform(post(BASE_CART_PATH + "/" + INVALID_EMAIL + "/" + VALID_ID))
        .andExpect(status().isNotFound());
  }


  @Test
  public void removeItemFromSavedCartReturns204() throws Exception {
    mockMvc.perform(delete(BASE_CART_PATH + "/" + VALID_EMAIL + "/" + VALID_ID))
        .andExpect(status().isNoContent());
  }

  @Test
  public void removeItemFromSavedCartReturns404InvalidEmail() throws Exception {
    mockMvc.perform(delete(BASE_CART_PATH + "/" + INVALID_EMAIL + "/" + VALID_ID))
        .andExpect(status().isNotFound());
  }


  @Test
  public void removeItemFromSavedCartReturns400EmptyCart() throws Exception {
    mockMvc.perform(delete(BASE_CART_PATH + "/email@email1.com" + "/" + VALID_ID))
        .andExpect(status().isBadRequest());
  }

  @Test
  public void emptyEntireCartReturns204() throws Exception {
    mockMvc.perform(delete(BASE_CART_PATH + "/" + VALID_EMAIL))
        .andExpect(status().isNoContent());
  }

  @Test
  public void emptyEntireCartReturns404InvalidEmail() throws Exception {
    mockMvc.perform(delete(BASE_CART_PATH + "/" + INVALID_EMAIL))
        .andExpect(status().isNotFound());
  }

  @Test
  public void emptyEntireCartReturns400EmptyCart() throws Exception {
    mockMvc.perform(delete(BASE_CART_PATH + "/email@email1.com"))
        .andExpect(status().isBadRequest());
  }

}
