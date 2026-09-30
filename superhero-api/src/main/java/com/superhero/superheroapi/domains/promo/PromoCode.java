package com.superhero.superheroapi.domains.promo;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.superhero.superheroapi.domains.product.Product;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a PromoCode entity.
 */
@Entity
@Data
@Table(name = "promo_code", schema = "public")
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class PromoCode {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank(message = "Title field is required")
  @Size(max = 20, message = "Title must be less than 20 characters")
  @Column(name = "title")
  private String title;

  //This field has been commented out until the functionality is fleshed out in a future card
//  @NotBlank(message = "Type field is required")
//  @Pattern(regexp = "\\$|%", message = "The type must be $ or %")
//  private String type;

  @NotBlank(message = "Description field is required")
  @Size(max = 100, message = "Description must be less than 100 characters")
  @Column(name = "description")
  private String description;

  @NotNull(message = "Rate field is required")
  @Column(name = "rate")
  private BigDecimal rate;

  @NotNull(message = "Active field is required")
  @Column(name = "active")
  private boolean active;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "promo_comics", joinColumns = @JoinColumn(name = "promo_id"),
      inverseJoinColumns = @JoinColumn(name = "product_id"),
      uniqueConstraints = @UniqueConstraint(columnNames = {"promo_id", "product_id"}))
  @JsonIgnore
  private List<Product> discountedComics = new ArrayList<>();

  private boolean sitewide;

}
