package com.superhero.superheroapi.domains.purchase;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.superhero.superheroapi.domains.product.Product;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.time.LocalDate;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "line_item", schema = "public")
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class LineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIgnore
    @JsonIgnoreProperties("products")
    private Purchase purchase;

    @ManyToOne
    @JsonIgnoreProperties("products")
    private Product product;

    private String condition;

    private int quantity;

    private LocalDate date;

    @Column(columnDefinition = "timestamp(0) without time zone")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTime;

}
