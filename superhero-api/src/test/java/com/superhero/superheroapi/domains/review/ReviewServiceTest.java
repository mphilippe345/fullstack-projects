package com.superhero.superheroapi.domains.review;

import com.superhero.superheroapi.domains.product.ProductService;
import com.superhero.superheroapi.domains.users.UserService;
import com.superhero.superheroapi.exception.BadRequest;
import com.superhero.superheroapi.exception.NotFound;
import com.superhero.superheroapi.exception.ServerError;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static com.superhero.superheroapi.domains.giftcard.GiftCardHelper.INVALID_ID;
import static com.superhero.superheroapi.domains.product.ProductHelper.VALID_ID;
import static com.superhero.superheroapi.domains.review.ReviewHelper.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {

  Review testReview;
  Review testReview2;

  Review testReviewWithPastDate;
  List<Review> reviewList = new ArrayList<>();
  List<Review> sortedReviewList = new ArrayList<>();
  @InjectMocks
  private ReviewService reviewService;
  @Mock
  private UserService userService;
  @Mock
  private ProductService productService;
  @Mock
  private ReviewRepository reviewRepository;

  @BeforeEach
  public void setUp() {
    testReview = generateValidReview();
    testReview2 = generateValidReview2();
    testReviewWithPastDate = generateValidReviewForSort();
    reviewList.addAll(Arrays.asList(testReview, testReview2));
    sortedReviewList.addAll(Arrays.asList(testReview, testReviewWithPastDate));
  }

  @Test
  public void getReviewsByProductIdReturnsReview() {
    List<Review> reviews = reviewRepository.findAllByProductId(VALID_ID);
    List<ReviewModel> expected = new ArrayList<>();
    reviews.forEach(review -> expected.add(mapReview(review)));

    List<ReviewModel> actual = reviewService.getMappedReviewsByProductId(VALID_ID, "newest");
    assertEquals(expected, actual,
        "Test expects the actual and expected list of products to be the same.");
  }

  @Test
  public void getReviewByInvalidProductIdThrowsNotFoundException() {
    when(productService.getProductById(INVALID_ID)).thenThrow(new NotFound("Simulated exception"));
    NotFound notFound = assertThrows(NotFound.class,
        () -> reviewService.getReviewsByProductId(INVALID_ID));
    assertEquals("Simulated exception", notFound.getMessage(),
        "The message in the error thrown do not match the one that's expected.");
  }

  @Test
  public void getReviewsByProductIdThrowsDataAccessException() {
    when(reviewRepository.findAllByProductId(anyLong())).thenThrow(
        new DataAccessException("Simulated exception") {
        });
    ServerError serverError = assertThrows(ServerError.class,
        () -> reviewService.getMappedReviewsByProductId(VALID_ID, "newest"));
    assertEquals("Simulated exception", serverError.getMessage(),
        "The message in the error thrown do not match the one that's expected.");
  }

  @Test
  public void getReviewsByEmailReturnsReview() {
    List<Review> reviews = reviewRepository.findAllByUserEmail(VALID_USER.getEmail());
    List<ReviewModel> expected = new ArrayList<>();
    reviews.forEach(review -> expected.add(mapReview(review)));

    List<ReviewModel> actual = reviewService.getReviewsByEmail(VALID_USER.getEmail());
    assertEquals(expected, actual,
        "Test expects the actual and expected list of products to be the same.");
  }

  @Test
  public void getSortedReviewsByProductIdReturnsNewestReview() {

    when(reviewRepository.findAllByProductId(VALID_ID)).thenReturn(sortedReviewList);

    List<ReviewModel> actualList = reviewService.getMappedReviewsByProductId(VALID_ID, "newest");

    List<LocalDate> expectedDates = sortedReviewList.stream()
        .sorted(Comparator.comparing(Review::getReviewDate).reversed())
        .map(Review::getReviewDate).toList();

    List<LocalDate> actualDates = actualList.stream()
        .map(ReviewModel::getReviewDate).toList();

    assertEquals(expectedDates, actualDates);

  }

  @Test
  public void getSortedReviewsByProductIdReturnsOldestReview() {
    when(reviewRepository.findAllByProductId(VALID_ID)).thenReturn(sortedReviewList);

    List<ReviewModel> actualList = reviewService.getMappedReviewsByProductId(VALID_ID, "oldest");

    List<LocalDate> expectedDates = sortedReviewList.stream()
        .sorted(Comparator.comparing(Review::getReviewDate))
        .map(Review::getReviewDate).toList();

    List<LocalDate> actualDates = actualList.stream()
        .map(ReviewModel::getReviewDate).toList();

    assertEquals(expectedDates, actualDates);
  }

  @Test
  public void getSortedReviewsByOldestRatingReturnsLowestRatedReview() {
    when(reviewRepository.findAllByProductId(VALID_ID)).thenReturn(sortedReviewList);

    List<ReviewModel> actualList = reviewService.getMappedReviewsByProductId(VALID_ID, "ratingLow");

    List<Integer> expectedRatings = sortedReviewList.stream()
            .sorted(Comparator.comparing(Review::getRating))
            .map(Review::getRating).toList();

    List<Integer> actualRatings = actualList.stream()
            .map(ReviewModel::getRating).toList();
    assertEquals(expectedRatings, actualRatings);
  }

  @Test
  public void getSortedReviewsByRatingReturnsHighestRatedReview() {

    when(reviewRepository.findAllByProductId(VALID_ID)).thenReturn(sortedReviewList);

    List<ReviewModel> actualList = reviewService.getMappedReviewsByProductId(VALID_ID, "ratingHigh");

    List<Integer> expectedRatings = sortedReviewList.stream()
            .sorted(Comparator.comparing(Review::getRating).reversed())
            .map(Review::getRating).toList();

    List<Integer> actualRatings = actualList.stream()
            .map(ReviewModel::getRating).toList();

    assertEquals(expectedRatings, actualRatings);

  }

  @Test
  public void getReviewByInvalidEmailThrowsNotFoundException() {
    when(userService.findByEmail(INVALID_EMAIL)).thenThrow(new NotFound("Simulated exception"));
    Throwable actual = assertThrows(NotFound.class,
        () -> reviewService.getReviewsByEmail(INVALID_EMAIL));
    Throwable expected = new NotFound(
        "Simulated exception");
    assertEquals(expected.getMessage(), actual.getMessage(),
        "The message in the error thrown do not match the one that's expected.");
  }

  @Test
  public void getReviewsByEmailThrowsDataAccessException() {
    when(reviewRepository.findAllByUserEmail(anyString())).thenThrow(
        new DataAccessException("Simulated exception") {
        });
    ServerError serverError = assertThrows(ServerError.class,
        () -> reviewService.getReviewsByEmail(INVALID_EMAIL));
    assertEquals("Simulated exception", serverError.getMessage(),
        "The message in the error thrown do not match the one that's expected.");
  }


  @Test
  public void creatingReviewReturnsReview() {
    when(reviewRepository.save(any(Review.class))).thenReturn(testReview);
    ReviewModel expected = mapReview(testReview);
    ReviewModel actual = reviewService.createReview(mapReview(testReview));
    assertEquals(expected, actual, "The Review objects do not match.");
  }

  @Test
  public void createReviewWithInvalidEmailThrowsNotFoundException() {
    when(userService.findByEmail(INVALID_EMAIL)).thenThrow(new NotFound("Simulated exception"));
    Throwable actual = assertThrows(NotFound.class,
        () -> reviewService.createReview(mapReview(generateReviewWithInvalidEmail())));
    Throwable expected = new NotFound(
        "Simulated exception");
    assertEquals(expected.getMessage(), actual.getMessage(),
        "The message in the error thrown do not match the one that's expected.");
  }

  @Test
  public void createReviewWithNoUserThrowsNotFoundException() {
    Throwable actual = assertThrows(BadRequest.class,
        () -> reviewService.createReview(mapReview(generateReviewWithNullUser())));
    Throwable expected = new NotFound(
        "User email for review must be provided.");
    assertEquals(expected.getMessage(), actual.getMessage(),
        "The message in the error thrown do not match the one that's expected.");
  }

  @Test
  public void createReviewWithNoProductThrowsNotFoundException() {
    Throwable actual = assertThrows(BadRequest.class,
        () -> reviewService.createReview(mapReview(generateReviewWithNullProduct())));
    Throwable expected = new NotFound(
        "Product for review must be provided.");
    assertEquals(expected.getMessage(), actual.getMessage(),
        "The message in the error thrown do not match the one that's expected.");
  }


  @Test
  public void createReviewsThrowsDataAccessException() {
    when(reviewRepository.save(any(Review.class))).thenThrow(
        new DataAccessException("Simulated exception") {
        });
    ServerError serverError = assertThrows(ServerError.class,
        () -> reviewService.createReview(mapReview(testReview)));
    assertEquals("Simulated exception", serverError.getMessage(),
        "The message in the error thrown do not match the one that's expected.");
  }

}