package com.superhero.superheroapi.domains.product;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.superhero.superheroapi.domains.promo.PromoCode;
import com.superhero.superheroapi.domains.review.Review;
import com.superhero.superheroapi.domains.sitewidesales.SiteWideSale;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product", schema = "public")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Product {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "title")
  private String title;

  @Column(name = "author")
  private String author;

  @Column(name = "description", columnDefinition = "TEXT")
  private String description;

  @Column(name = "genre")
  private String genre;

  @Column(name = "image_url")
  private String imageUrl;

  @Column(name = "issue_number")
  private Integer issue;

  @Column(name = "volume_id")
  private String volume;

  @Column(name = "newrelease")
  private Boolean newrelease;

  @Column(name = "publisher")
  private String publisher;

  @Column(name = "stock_status")
  private Boolean stockStatus;

  @Column(name = "active")
  private Boolean active;

  @Column(name = "sku")
  private String sku;

  @Column(name = "release_date")
  private LocalDate releaseDate;

  @Column(name = "price")
  private BigDecimal price;

  @Column(name = "best_seller")
  private Boolean bestSeller;

  @OneToMany(fetch = FetchType.EAGER, mappedBy = "product", cascade = {CascadeType.DETACH,
      CascadeType.MERGE, CascadeType.REFRESH, CascadeType.REMOVE})
  @JsonManagedReference
  private List<Review> reviews;

  @ManyToMany(mappedBy = "discountedComics", fetch = FetchType.LAZY)
  @JsonIgnore
  private List<PromoCode> promos = new ArrayList<>();

  @ManyToMany(mappedBy = "discountedProducts", fetch = FetchType.LAZY)
  @JsonIgnore
  private List<SiteWideSale> siteWideSales = new ArrayList<>();

}
