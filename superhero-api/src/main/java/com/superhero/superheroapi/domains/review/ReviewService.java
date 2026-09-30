package com.superhero.superheroapi.domains.review;

import com.superhero.superheroapi.domains.product.Product;
import com.superhero.superheroapi.domains.product.ProductService;
import com.superhero.superheroapi.domains.users.User;
import com.superhero.superheroapi.domains.users.UserService;
import com.superhero.superheroapi.exception.BadRequest;
import com.superhero.superheroapi.exception.ServerError;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ReviewService implements IReviewService {

    private final Logger logger = LoggerFactory.getLogger(ReviewService.class);

    private final ReviewRepository reviewRepository;

    private final UserService userService;

    private final ProductService productService;

    private final ModelMapper mapper = new ModelMapper();

    /**
     * gets all the reviews from the database by product ids.
     *
     * @param id product id of the reviews requested
     * @return list of review entities.
     */
    public List<Review> getReviewsByProductId(Long id) {
        List<Review> allReviews;

        productService.getProductById(id);

        try {
            allReviews = reviewRepository.findAllByProductId(id);
        } catch (DataAccessException e) {
            logger.error(e.getMessage());
            throw new ServerError(e.getMessage());
        }

        allReviews.sort(Comparator.comparing(Review::getReviewDate).reversed());
        return allReviews;

    }

    /**
     * maps all the reviews from the database by product ids to ReviewModel and sorts if a sort was provided.
     *
     * @param id   product id of the reviews requested.
     * @param sort desired sort for the reviews.
     * @return list of reviews mapped for the frontend.
     */
    @Override
    public List<ReviewModel> getMappedReviewsByProductId(Long id, String sort) {
        List<Review> reviews = getReviewsByProductId(id);
        if (sort != null) {
            switch (sort) {
                case "oldest" -> reviews.sort(Comparator.comparing(Review::getReviewDate));
                case "ratingHigh" -> reviews.sort(Comparator.comparing(Review::getRating).reversed());
                case "ratingLow" -> reviews.sort(Comparator.comparing(Review::getRating));
                default -> reviews.sort(Comparator.comparing(Review::getReviewDate).reversed());
            }
        }
        return getReviewModels(reviews);
    }

    /**
     * returns all reviews by user email.
     *
     * @param email email of the user.
     * @return reviews that have a user with the matching email.
     */
    @Override
    public List<ReviewModel> getReviewsByEmail(String email) {
        userService.findByEmail(email);
        try {
            return getReviewModels(reviewRepository.findAllByUserEmail(email));
        } catch (DataAccessException e) {
            logger.error(e.getMessage());
            throw new ServerError(e.getMessage());
        }
    }

    /**
     * maps a list of reviews to the ReviewModel class.
     *
     * @param allReviews reviews to be mapped
     * @return list of ReviewModel objects.
     */
    private List<ReviewModel> getReviewModels(List<Review> allReviews) {
        List<ReviewModel> mappedReviews = new ArrayList<>();

        allReviews.forEach(review -> {
            ReviewModel mappedReview = mapper.map(review, ReviewModel.class);
            mappedReview.setUserEmail(review.getUser().getEmail());
            mappedReviews.add(mappedReview);
        });

        return mappedReviews;
    }

    /**
     * Fetches all reviews by product id, gets all the ratings from them and calculates the average
     *
     * @param id id of the product that is being determined an average rating.
     * @return an Integer as the average rating
     */
    @Override
    public Integer getAverageRatingByProductId(Long id) {
        List<Review> allReviews = getReviewsByProductId(id);
        var ratings = new ArrayList<>();

        for (Review review : allReviews) {
            int rating = review.getRating();
            ratings.add(rating);
        }

        int sum = 0;
        int length = ratings.size();

        for (var rate : ratings) {
            sum = sum + (Integer) rate;
        }

        return sum / length;
    }

    /**
     * Creates a review and persists the data in the database.
     *
     * @param review Review that is being created
     * @return Review that was saved to the database.
     */
    @Override
    public ReviewModel createReview(ReviewModel review) {
        if (review.getUserEmail() == null) {
            logger.error("User email for review must be provided.");
            throw new BadRequest("User email for review must be provided.");
        }
        if (review.getProductId() == null) {
            logger.error("Product for review must be provided.");
            throw new BadRequest("Product for review must be provided.");
        }

        Product product = productService.getProductById(review.getProductId());

        User user = userService.findByEmail(review.getUserEmail());

        Review reviewToSave = new Review();
        reviewToSave.setReviewDate(LocalDate.now());
        reviewToSave.setComment(review.getComment());
        reviewToSave.setTitle(review.getTitle());
        reviewToSave.setRating(review.getRating());
        reviewToSave.setUser(user);
        reviewToSave.setProduct(product);

        ReviewModel mappedReview;

        try {
            mappedReview = mapper.map(reviewRepository.save(reviewToSave), ReviewModel.class);
            mappedReview.setUserEmail(review.getUserEmail());
            mappedReview.setProductId(review.getProductId());
        } catch (DataAccessException e) {
            logger.error(e.getMessage());
            throw new ServerError(e.getMessage());
        }

        return mappedReview;
    }

}
