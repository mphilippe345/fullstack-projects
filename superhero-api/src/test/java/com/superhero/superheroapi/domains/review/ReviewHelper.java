package com.superhero.superheroapi.domains.review;

import static com.superhero.superheroapi.domains.product.ProductHelper.generateValidProductWithGenre1;

import com.superhero.superheroapi.domains.cart.CartItem;
import com.superhero.superheroapi.domains.product.Product;
import com.superhero.superheroapi.domains.users.User;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.modelmapper.ModelMapper;

public class ReviewHelper {

  public static final String BASE_REVIEW_PATH = "/api/reviews";
  public static final Long VALID_ID = 1L;
  public static final Long VALID_ID_2 = 2L;
  public static final Long INVALID_ID = 0L;
  public static final String VALID_EMAIL = "email@email.com";
  public static final String INVALID_EMAIL = "invalid@email.com";
  public static final String VALID_TITLE = "Test Title";
  public static final String VALID_COMMENT = "Test Comment";
  public static final Product VALID_PRODUCT = generateValidProductWithGenre1();
  public static final List<Review> REVIEW_LIST = new ArrayList<>();
  public static final User VALID_USER = new User(VALID_ID, "John", "Doe", VALID_EMAIL, new CartItem(),
      REVIEW_LIST);
  public static final User INVALID_USER = new User(VALID_ID, "John", "Doe", INVALID_EMAIL,
      new CartItem(), REVIEW_LIST);
  public static final Integer VALID_RATING = 5;
  public static final Integer VALID_RATING_2 = 1;
  public static final LocalDate VALID_DATE = LocalDate.now();

  public static final LocalDate Past_Date = LocalDate.of(2020, 1, 5);


  public static Review generateValidReview() {
    return new Review(VALID_ID, VALID_PRODUCT, VALID_USER, VALID_DATE, VALID_RATING, VALID_TITLE,
        VALID_COMMENT);
  }

  public static Review generateValidReview2() {
    return new Review(VALID_ID_2, VALID_PRODUCT, VALID_USER, VALID_DATE, VALID_RATING_2,
        VALID_TITLE, VALID_COMMENT);
  }

  public static Review generateValidReviewForSort() {
    return new Review(VALID_ID, VALID_PRODUCT, VALID_USER, Past_Date, VALID_RATING_2, VALID_TITLE,
        VALID_COMMENT);
  }

  public static Review generateReviewWithInvalidEmail() {
    return new Review(VALID_ID, VALID_PRODUCT, INVALID_USER, VALID_DATE, VALID_RATING, VALID_TITLE,
        VALID_COMMENT);
  }

  public static Review generateReviewWithNullProduct() {
    return new Review(VALID_ID, null, INVALID_USER, VALID_DATE, VALID_RATING, VALID_TITLE,
        VALID_COMMENT);
  }

  public static Review generateReviewWithNullUser() {
    return new Review(VALID_ID, VALID_PRODUCT, null, VALID_DATE, VALID_RATING, VALID_TITLE,
        VALID_COMMENT);
  }

  public static ReviewModel mapReview(Review review) {
    ModelMapper mapper = new ModelMapper();
    return mapper.map(review, ReviewModel.class);
  }
}
