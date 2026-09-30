package com.superhero.superheroapi.domains.promo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * Represents the association between a PromoCode and a Comic in the promo_comics table.
 */
@Entity
@Data
@Table(name = "promo_comics", schema = "public")
public class DiscountedComics {

  /**
   * The unique identifier of the discounted comic association.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  /**
   * The ID of the PromoCode associated with the discounted comic.
   */
  @Column(name = "promo_id")
  private Long promo_id;

  /**
   * The ID of the Comic associated with the promoCode.
   */
  @Column(name = "product_id")
  private Long product_id;

  // Getters and Setters omitted for brevity
}
