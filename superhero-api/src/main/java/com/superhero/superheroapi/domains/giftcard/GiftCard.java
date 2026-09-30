package com.superhero.superheroapi.domains.giftcard;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GiftCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String code;

    @Temporal(TemporalType.TIMESTAMP)
    Date purchaseDate;

    @Setter(AccessLevel.NONE)
    BigDecimal balance;

    Boolean active;

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
        updateActive();
    }

    private void updateActive() {
        this.active = balance.compareTo(BigDecimal.ZERO) > 0;
    }
}
