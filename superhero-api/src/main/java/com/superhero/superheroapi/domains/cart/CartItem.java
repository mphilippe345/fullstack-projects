package com.superhero.superheroapi.domains.cart;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.superhero.superheroapi.domains.product.Product;
import com.superhero.superheroapi.domains.users.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cart_item", schema = "public")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  @ManyToOne
  private Product product;
  @ManyToOne
  @JoinColumn(name = "comic_user_id")
  private User user;

  private int quantity;

  private BigDecimal cost;

  private BigDecimal cartTotal;

}
