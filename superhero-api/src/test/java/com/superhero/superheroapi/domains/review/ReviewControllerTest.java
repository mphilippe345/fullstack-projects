package com.superhero.superheroapi.domains.review;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static com.superhero.superheroapi.domains.review.ReviewHelper.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
public class ReviewControllerTest {

  private static MockMvc mockMvc;
  @Autowired
  ReviewRepository reviewRepository;
  Review testReview;
  ReviewModel reviewModel;
  ReviewModel invalidEmailModel;
  ObjectMapper objectMapper = new ObjectMapper();
  @Autowired
  private WebApplicationContext wac;

  @BeforeEach
  public void setup() {
    mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    objectMapper.registerModule(new JavaTimeModule());

    testReview = reviewRepository.save(generateValidReview());
    reviewModel = mapReview(testReview);

    invalidEmailModel = mapReview(generateReviewWithInvalidEmail());
  }

  @Test
  public void getReviewByProductIdReturns200() throws Exception {
    mockMvc.perform(get(BASE_REVIEW_PATH + "/product/" + VALID_PRODUCT.getId()))
        .andExpect(status().isOk());
  }

  @Test
  public void getReviewByProductIdWithNonExistentIdReturns404() throws Exception {
    mockMvc.perform(get(BASE_REVIEW_PATH + "/product/" + INVALID_ID))
        .andExpect(status().isNotFound());
  }

  @Test
  public void getReviewByUserEmailReturns200() throws Exception {
    mockMvc.perform(get(BASE_REVIEW_PATH + "/email/" + VALID_EMAIL))
        .andExpect(status().isOk());
  }

  @Test
  public void getReviewByEmailWithInvalidEmailReturns404() throws Exception {
    mockMvc.perform(get(BASE_REVIEW_PATH + "/email/" + INVALID_ID))
        .andExpect(status().isNotFound());
  }

  @Test
  public void getSortedReviewsByProductIdInvalidId404() throws Exception {
    mockMvc.perform(get(BASE_REVIEW_PATH + "/" + INVALID_ID + "/newest"))
        .andExpect(status().isNotFound());
  }

  @Test
  public void createReviewReturns201() throws Exception {
    mockMvc.perform(post(BASE_REVIEW_PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(reviewModel)))
        .andExpect(status().isCreated());
  }

  @Test
  public void createReviewWithNoUserReturns400() throws Exception {
    ReviewModel invalidReview = mapReview(generateReviewWithNullUser());
    mockMvc.perform(post(BASE_REVIEW_PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(invalidReview)))
        .andExpect(status().isBadRequest());
  }

  @Test
  public void createReviewWithNoProductReturns400() throws Exception {
    ReviewModel invalidReview = mapReview(generateReviewWithNullProduct());
    mockMvc.perform(post(BASE_REVIEW_PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(invalidReview)))
        .andExpect(status().isBadRequest());
  }


  @Test
  public void createReviewWithInvalidEmailReturns404() throws Exception {
    mockMvc.perform(post(BASE_REVIEW_PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(invalidEmailModel)))
        .andExpect(status().isNotFound());
  }

  @Test
  public void createReviewWithInvalidProductReturns404() throws Exception {
    reviewModel.setProductId(0L);
    mockMvc.perform(post(BASE_REVIEW_PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(reviewModel)))
        .andExpect(status().isNotFound());
  }


}