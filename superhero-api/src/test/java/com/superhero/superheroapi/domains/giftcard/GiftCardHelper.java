package com.superhero.superheroapi.domains.giftcard;

import java.math.BigDecimal;
import java.util.Date;

public class GiftCardHelper {

    public static final String BASE_GIFT_CARD_PATH = "/api/gift-cards";

    public static final Long VALID_ID = 1L;

    public static final Long INVALID_ID = 0L;
    public static final String VALID_TEST_CODE = "SLD3-435F-SK3T";

    public static final String INVALID_TEST_CODE = "AJSDIODFJM";
    public static final Date VALID_DATE = new Date();
    public static final BigDecimal VALID_BALANCE = new BigDecimal("50");

    public static final BigDecimal INVALID_BALANCE = new BigDecimal("0");

    public static GiftCard generateValidGiftCard() {
        return new GiftCard(VALID_ID, VALID_TEST_CODE, VALID_DATE, VALID_BALANCE, true);
    }
}
