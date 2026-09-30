package com.superhero.superheroapi.domains.giftcard;

import java.math.BigDecimal;

public interface IGiftCardService {
    GiftCard getGiftCardById(Long id);

    GiftCard getGiftCardByCode(String code);

    GiftCard generateGiftCard(BigDecimal value);
}
