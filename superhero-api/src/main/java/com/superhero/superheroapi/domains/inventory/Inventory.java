package com.superhero.superheroapi.domains.inventory;

import com.superhero.superheroapi.domains.product.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Setter;

@Entity
@Data
@Table(name = "inventory", schema = "public")
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "condition")
    private String condition;

    @Column(name = "inventory_count") // sql accessor would be inventory.inventory_count; redundant?
    private int amount; // inconsistent

    @Column(name = "added_to_inventory")
    private LocalDate dateAdded;

    @Column(name = "rate")
    private BigDecimal rate;

    public BigDecimal getConditionPrice() {
        return this.product.getPrice().multiply(this.rate);
    }

}
