package com.superhero.superheroapi.domains.review;

import java.util.List;

public interface IReviewService {

    List<ReviewModel> getMappedReviewsByProductId(Long id, String sort);

    List<ReviewModel> getReviewsByEmail(String email);

    Integer getAverageRatingByProductId(Long id);

    ReviewModel createReview(ReviewModel review);
}
