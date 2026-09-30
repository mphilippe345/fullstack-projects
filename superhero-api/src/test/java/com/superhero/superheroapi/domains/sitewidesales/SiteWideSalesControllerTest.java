package com.superhero.superheroapi.domains.sitewidesales;

import static com.superhero.superheroapi.domains.product.ProductHelper.generateValidProductModel;
import static com.superhero.superheroapi.domains.sitewidesales.SiteWideSalesHelper.BASE_SITE_WIDE_SALES_PATH;
import static com.superhero.superheroapi.domains.sitewidesales.SiteWideSalesHelper.INVALID_ID;
import static com.superhero.superheroapi.domains.sitewidesales.SiteWideSalesHelper.VALID_ID;
import static com.superhero.superheroapi.domains.sitewidesales.SiteWideSalesHelper.VALID_SECOND_ID;
import static com.superhero.superheroapi.domains.sitewidesales.SiteWideSalesHelper.generateInvalidSitewideSale;
import static com.superhero.superheroapi.domains.sitewidesales.SiteWideSalesHelper.generateValidActiveSitewideSale;
import static com.superhero.superheroapi.domains.sitewidesales.SiteWideSalesHelper.generateValidActiveSitewideSale2;
import static com.superhero.superheroapi.domains.sitewidesales.SiteWideSalesHelper.generateValidDiscountProducts;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.superhero.superheroapi.domains.product.ProductService;
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
public class SiteWideSalesControllerTest {

  @Autowired
  private WebApplicationContext wac;

  @Autowired
  ProductService productService;

  @Autowired
  SiteWideSalesService sitewideSalesService;

  @Autowired
  SiteWideSalesRepository siteWideSalesRepository;

  private static MockMvc mockMvc;

  ObjectMapper objectMapper = new ObjectMapper();

  SiteWideSale activeSiteWideSale;
  SiteWideSale activeSiteWideSale2;
  SiteWideSale activeSiteWideSale3;
  SiteWideSale invalidActiveSiteWideSale;
  DiscountedProducts discountedProducts;

  @BeforeEach
  public void setUp() {
    activeSiteWideSale = generateValidActiveSitewideSale();
    activeSiteWideSale2 = generateValidActiveSitewideSale2();
    invalidActiveSiteWideSale = generateInvalidSitewideSale();
    discountedProducts = generateValidDiscountProducts();
    mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    objectMapper.registerModule(new JavaTimeModule());
    activeSiteWideSale3 = siteWideSalesRepository.save(activeSiteWideSale2);
  }

  @Test
  public void getAllSiteWideSalesReturns200() throws Exception {
    mockMvc.perform(get(BASE_SITE_WIDE_SALES_PATH))
        .andExpect(status().isOk());
  }

  @Test
  public void createSiteWideSaleReturns201() throws Exception {
    mockMvc.perform(post(BASE_SITE_WIDE_SALES_PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(activeSiteWideSale)))
        .andExpect(status().isCreated());
  }

  @Test
  public void createSiteWideSaleReturns400() throws Exception {
    mockMvc.perform(post(BASE_SITE_WIDE_SALES_PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(invalidActiveSiteWideSale)))
        .andExpect(status().isBadRequest());
  }

  @Test
  public void updateSiteWideSaleByIdReturns201() throws Exception {
    sitewideSalesService.createSiteWideSale(activeSiteWideSale);
    System.out.println(objectMapper.writeValueAsString(activeSiteWideSale));
    mockMvc.perform(put(BASE_SITE_WIDE_SALES_PATH + "/" + VALID_ID)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(activeSiteWideSale)))
        .andExpect(status().isCreated());
  }

  @Test
  public void updateSiteWideSaleByIdReturns400() throws Exception {
    sitewideSalesService.createSiteWideSale(activeSiteWideSale);
    mockMvc.perform(put(BASE_SITE_WIDE_SALES_PATH + "/" + VALID_ID)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(invalidActiveSiteWideSale)))
        .andExpect(status().isBadRequest());
  }

  @Test
  public void deleteSiteWideSaleByIdReturns200() throws Exception {
    sitewideSalesService.createSiteWideSale(activeSiteWideSale2);
    mockMvc.perform(delete(BASE_SITE_WIDE_SALES_PATH + "/" + VALID_SECOND_ID))
        .andExpect(status().isNoContent());
  }

  @Test
  public void deleteSiteWideSaleByIdReturns400() throws Exception {
    mockMvc.perform(delete(BASE_SITE_WIDE_SALES_PATH + "/" + INVALID_ID))
        .andExpect(status().isNotFound());
  }

  @Test
  public void assignSiteWideSaleReturns201() throws Exception {
    sitewideSalesService.createSiteWideSale(activeSiteWideSale);
    productService.createProduct(generateValidProductModel());
    mockMvc.perform(post(BASE_SITE_WIDE_SALES_PATH + "/" + VALID_ID + "/product/" + VALID_ID))
        .andExpect(status().isCreated());
  }

  @Test
  public void assignSiteWideSaleReturns404() throws Exception {
    productService.createProduct(generateValidProductModel());
    mockMvc.perform(post(BASE_SITE_WIDE_SALES_PATH + "/" + INVALID_ID + "/product/" + VALID_ID))
        .andExpect(status().isNotFound());
  }
}
