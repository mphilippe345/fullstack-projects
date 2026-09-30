package com.superhero.superheroapi.domains.promo;


import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscountComicsRepository extends JpaRepository<DiscountedComics, Long> {

  @Query("SELECT d FROM DiscountedComics d")
  Set<DiscountedComics> findAllDiscountedComics();

  @Query("SELECT dc FROM DiscountedComics dc WHERE dc.product_id = :productId")
  List<DiscountedComics> findAllByProductId(@Param("productId") Long productId);
}
