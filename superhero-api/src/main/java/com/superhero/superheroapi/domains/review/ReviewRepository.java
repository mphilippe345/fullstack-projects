package com.superhero.superheroapi.domains.review;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query(value = "SELECT * FROM public.review WHERE product_id = :productId", nativeQuery = true)
    List<Review> findAllByProductId(@Param("productId") Long productId);

    List<Review> findAllByUserEmail(String userEmail);
}
