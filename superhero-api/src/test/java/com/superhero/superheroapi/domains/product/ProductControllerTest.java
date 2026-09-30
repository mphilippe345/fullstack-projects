package com.superhero.superheroapi.domains.product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static com.superhero.superheroapi.domains.product.ProductHelper.BASE_PRODUCTS_PATH;
import static com.superhero.superheroapi.domains.product.ProductHelper.generateValidProductWithGenre1;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
public class ProductControllerTest {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    ProductRepository productRepository;

    private static MockMvc mockMvc;

    Product product;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        product = productRepository.save(generateValidProductWithGenre1());
    }

    @Test
    public void getRelatedProductsReturns200() throws Exception {
        mockMvc.perform(get(BASE_PRODUCTS_PATH + "/related/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void getProductStatusesReturns200() throws Exception {
        mockMvc.perform(get(BASE_PRODUCTS_PATH + "/1/statuses"))
                .andExpect(status().isOk());
    }

    @Test
    public void getProductStatusesReturns404() throws Exception {
        mockMvc.perform(get(BASE_PRODUCTS_PATH + "/0/statuses"))
                .andExpect(status().isNotFound());
    }

}

