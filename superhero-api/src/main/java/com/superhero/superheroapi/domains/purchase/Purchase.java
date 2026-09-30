package com.superhero.superheroapi.domains.purchase;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "purchase", schema = "public")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Purchase {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToMany(mappedBy = "purchase")
  private Set<LineItem> products;

  @Valid
  @Embedded
  private PersonalInfo personalInfo;

  @Valid
  @Embedded
  @AttributeOverrides({
      @AttributeOverride(name = "streetAddress1", column = @Column(name = "shipping_street_address1")),
      @AttributeOverride(name = "streetAddress2", column = @Column(name = "shipping_street_address2")),
      @AttributeOverride(name = "city", column = @Column(name = "shipping_city")),
      @AttributeOverride(name = "state", column = @Column(name = "shipping_state")),
      @AttributeOverride(name = "zipCode", column = @Column(name = "shipping_zip_code")),
  })
  private Address shippingAddress;

  @Valid
  @Embedded
  @AttributeOverrides({
      @AttributeOverride(name = "streetAddress1", column = @Column(name = "billing_street_address1")),
      @AttributeOverride(name = "streetAddress2", column = @Column(name = "billing_street_address2")),
      @AttributeOverride(name = "city", column = @Column(name = "billing_city")),
      @AttributeOverride(name = "state", column = @Column(name = "billing_state")),
      @AttributeOverride(name = "zipCode", column = @Column(name = "billing_zip_code")),
  })
  private Address billingAddress;

  @Valid
  @Embedded
  private CreditCard creditCard;

  @Valid
  private BigDecimal orderTotal;

  private String orderNumber;

  private LocalDate date;

  @Enumerated(EnumType.STRING)
  private OrderStatus orderStatus;

  private static final BigDecimal shippingRate = new BigDecimal("2.00");

}

