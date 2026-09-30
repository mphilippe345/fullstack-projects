package com.superhero.superheroapi.domains.giftcard;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GiftCardRepository extends JpaRepository<GiftCard, Long> {

    Optional<GiftCard> findGiftCardByCode(String code);

    boolean existsByCode(String code);
}
