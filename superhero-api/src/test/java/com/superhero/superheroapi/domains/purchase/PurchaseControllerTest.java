package com.superhero.superheroapi.domains.purchase;

import static com.superhero.superheroapi.domains.product.ProductHelper.generateValidProductModelWithGenre1;
import static com.superhero.superheroapi.domains.purchase.PurchaseHelper.BASE_PURCHASES_PATH;
import static com.superhero.superheroapi.domains.purchase.PurchaseHelper.VALID_FIRST_NAME;
import static com.superhero.superheroapi.domains.purchase.PurchaseHelper.VALID_LAST_NAME;
import static com.superhero.superheroapi.domains.purchase.PurchaseHelper.generateInvalidPurchaseController;
import static com.superhero.superheroapi.domains.purchase.PurchaseHelper.generateValidPurchaseController;
import static com.superhero.superheroapi.domains.product.ProductHelper.generateValidProductModel;
import static com.superhero.superheroapi.domains.purchase.PurchaseHelper.INVALID_EMAIL;
import static com.superhero.superheroapi.domains.purchase.PurchaseHelper.VALID_EMAIL;
import static com.superhero.superheroapi.domains.purchase.PurchaseHelper.generateValidPurchaseSet;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.superhero.superheroapi.domains.product.ProductService;
import com.superhero.superheroapi.domains.users.User;
import com.superhero.superheroapi.domains.users.UserRepository;
import com.superhero.superheroapi.domains.users.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@ActiveProfiles("test")
@SpringBootTest
public class PurchaseControllerTest {
  @Autowired
  private WebApplicationContext wac;

  @Autowired
  ProductService productService;

  @Autowired
  UserService userService;

  @Mock
  PurchaseRepository purchaseRepository;


  private static MockMvc mockMvc;

  Purchase validPurchase;
  Purchase invalidPurchase;

  ObjectMapper objectMapper = new ObjectMapper();
  @BeforeEach
  public void setUp() {
    validPurchase = generateValidPurchaseController();
    invalidPurchase = generateInvalidPurchaseController();
    purchaseRepository.save(generateValidPurchaseSet());
    mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    objectMapper.registerModule(new JavaTimeModule());
  }

  @Test
  public void createPurchaseReturns201() throws Exception {
    productService.createProduct(generateValidProductModel());
    mockMvc.perform(post(BASE_PURCHASES_PATH)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(validPurchase)))
        .andExpect(status().isCreated());
  }

  @Test
  public void postInvalidPurchaseReturns400() throws Exception {
    productService.createProduct(generateValidProductModelWithGenre1());
    mockMvc.perform(post(BASE_PURCHASES_PATH)
            .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(invalidPurchase)))
        .andExpect(status().isBadRequest());
  }

  @Test
  public void getByValidEmailReturns200() throws Exception {
    userService.save(new User(VALID_FIRST_NAME, VALID_LAST_NAME, VALID_EMAIL));
    mockMvc.perform(get(BASE_PURCHASES_PATH + "/" + VALID_EMAIL + "?page=0&size=2"))
        .andExpect(status().isOk());
  }

  @Test
  public void getByInValidEmailReturns404() throws Exception {
    mockMvc.perform(get(BASE_PURCHASES_PATH + "/" + INVALID_EMAIL + "?page=0&size=2"))
        .andExpect(status().isNotFound());
  }
}
