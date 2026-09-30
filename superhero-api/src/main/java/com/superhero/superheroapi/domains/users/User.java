package com.superhero.superheroapi.domains.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.superhero.superheroapi.domains.cart.CartItem;
import com.superhero.superheroapi.domains.review.Review;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@RequiredArgsConstructor
@Table(name = "comic_user")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @NonNull
  @Column(name = "first_name")
  private String firstName;

  @NonNull
  @Column(name = "last_name")
  private String lastName;

  @NonNull
  @Column(name = "email", unique = true, nullable = false)
  private String email;
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  @JsonIgnore
  private List<CartItem> cartItems;

  public BigDecimal getCartTotal() {
    return this.cartItems.stream()
        .map(item -> item.getCost().multiply(BigDecimal.valueOf(item.getQuantity())))
        .reduce(BigDecimal.ZERO, BigDecimal::add)
        .setScale(2, RoundingMode.HALF_UP);
  }


  @OneToMany(fetch = FetchType.EAGER, mappedBy = "user", cascade = CascadeType.ALL)
  @JsonIgnore
  private List<Review> reviews;
}
