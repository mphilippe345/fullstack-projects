package com.superhero.superheroapi.domains.review;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.superhero.superheroapi.constants.Paths.BASE;
import static com.superhero.superheroapi.constants.Paths.REVIEW_ENDPOINT;

@RestController
@RequiredArgsConstructor
@RequestMapping(BASE + REVIEW_ENDPOINT)
public class ReviewController {

    private final Logger logger = LoggerFactory.getLogger(ReviewController.class);

    private final IReviewService reviewService;

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ReviewModel>> getReviewsByProductId(@PathVariable Long productId, @RequestParam(required = false) String sort) {
        logger.info("Returning reviews with productID: " + productId + "...");
        return new ResponseEntity<>(reviewService.getMappedReviewsByProductId(productId, sort),
                HttpStatus.OK);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<List<ReviewModel>> getReviewsByEmail(@PathVariable String email) {
        logger.info("Returning reviews by user email: " + email + "...");
        return new ResponseEntity<>(reviewService.getReviewsByEmail(email), HttpStatus.OK);
    }

    @GetMapping("/{productId}/average")
    public ResponseEntity<Integer> getAverageRatingByProductId(@PathVariable Long productId) {
        logger.info("Returning average rating of productID: " + productId + "...");
        return new ResponseEntity<>(reviewService.getAverageRatingByProductId(productId),
                HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<ReviewModel> createReview(@RequestBody ReviewModel review) {
        logger.info("Saving new review...");
        return new ResponseEntity<>(reviewService.createReview(review), HttpStatus.CREATED);
    }

}
