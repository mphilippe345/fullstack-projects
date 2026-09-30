package com.superhero.superheroapi.domains.sitewidesales;

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
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "sitewide-sales", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
public class SiteWideSale {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank(message = "Name is required.")
  @Size(max = 20, message = "Name must be less than 20 characters")
  private String name;

  @NotBlank(message = "Type is required.")
  @Pattern(regexp = "[$%]+", message = "Type must be either $ or %.")
  private String type;

  @NotNull(message = "Rate is required.")
  @DecimalMin(value = "0", inclusive = false, message = "Rate must be higher than 0.")
  @DecimalMax(value = "100", message = "Rate must be less than 100.")
  private BigDecimal rate;

  @NotNull(message = "Start Date is required.")
  private LocalDateTime startDate;

  @NotNull(message = "End Date is required.")
  private LocalDateTime endDate;

  @AssertTrue(message = "End Date must be the same or after Start Date.")
  private boolean isEndDateAfterStartDate() {
    return startDate == null || endDate == null || !endDate.isBefore(startDate);
  }

  @AssertTrue(message = "Start Date must be the same or after Created Date.")
  private boolean isStartDateAfterCreatedDate() {
    return startDate == null || createdDate == null || !startDate.isBefore(createdDate);
  }

  @NotNull(message = "Created Date is required.")
  @PastOrPresent(message = "Created Date must be in the past or present.")
  private LocalDateTime createdDate;

  @NotNull(message = "isActive field is required.")
  private boolean isActive;


  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "sale_comics", joinColumns = @JoinColumn(name = "sitewide_sale_id"),
      inverseJoinColumns = @JoinColumn(name = "product_id"),
      uniqueConstraints = @UniqueConstraint(columnNames = {"sitewide_sale_id", "product_id"}))
  @JsonIgnore
  private List<Product> discountedProducts = new ArrayList<>();

  private boolean sitewide;
}
