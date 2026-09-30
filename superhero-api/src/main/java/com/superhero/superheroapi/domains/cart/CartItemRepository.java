package com.superhero.superheroapi.domains.cart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
  @Query("SELECT ci FROM CartItem ci WHERE ci.user.id = :userId AND ci.product.id = :productId")
  CartItem findDuplicateItemsInCart(@Param("userId") Long userId, @Param("productId") Long productId);
}
