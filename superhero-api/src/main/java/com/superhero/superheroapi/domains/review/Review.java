package com.superhero.superheroapi.domains.review;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.superhero.superheroapi.domains.product.Product;
import com.superhero.superheroapi.domains.users.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "review", schema = "public")
@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@ToString
public class Review {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "product_id")
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    @JsonBackReference
    private Product product;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "user_email", referencedColumnName = "email", nullable = false)
    private User user;

    @Column(name = "review_date")
    private LocalDate reviewDate;

    @Column(name = "rating")
    private Integer rating;

    @Column(name = "title", length = 50)
    private String title;

    @Column(name = "comment", length = 500)
    private String comment;
}
