package com.superhero.superheroapi.domains.sitewidesales;

import com.superhero.superheroapi.domains.product.Product;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SiteWideSalesHelper {

  public static final String BASE_SITE_WIDE_SALES_PATH = "/api/sitewide-sales";

  public static final Long VALID_ID = 1L;
  public static final Long VALID_SECOND_ID = 2L;
  public static final Long INVALID_ID = 0L;

  public static final String VALID_NAME = "halloween23";
  public static final String VALID_NAME2 = "october23";
  public static final String INVALID_NAME = null;

  public static final String VALID_TYPE = "$";

  public static final BigDecimal VALID_RATE = BigDecimal.valueOf(0.15);

  public static final LocalDateTime VALID_START_DATE = LocalDateTime.of(2025, 10, 12, 00, 00, 00);
  public static final LocalDateTime VALID_START_DATE_TODAY = LocalDateTime.now();
  public static final LocalDateTime VALID_END_DATE = LocalDateTime.of(2025, 10, 31, 23, 59, 59);

  public static final LocalDateTime VALID_CREATION_DATE = LocalDateTime.now();

  public static final boolean VALID_ACTIVE = true;
  public static final boolean VALID_INACTIVE = false;

  public static final boolean FALSE_SITEWIDE = false;

  public static final List<Product> discountedProducts = new ArrayList<>();

  public static SiteWideSale generateValidActiveSitewideSale() {
    return new SiteWideSale(VALID_ID, VALID_NAME, VALID_TYPE, VALID_RATE, VALID_START_DATE,
        VALID_END_DATE, VALID_CREATION_DATE, VALID_ACTIVE, discountedProducts, FALSE_SITEWIDE);
  }

  public static SiteWideSale generateValidActiveSitewideSale2() {
    return new SiteWideSale(VALID_SECOND_ID, VALID_NAME2, VALID_TYPE, VALID_RATE, VALID_START_DATE,
        VALID_END_DATE, VALID_CREATION_DATE, VALID_ACTIVE, discountedProducts, FALSE_SITEWIDE);
  }

  public static SiteWideSale generateInvalidActiveSitewideSale() {
    return new SiteWideSale(INVALID_ID, VALID_NAME, VALID_TYPE, VALID_RATE, VALID_START_DATE,
        VALID_END_DATE, VALID_CREATION_DATE, VALID_ACTIVE, discountedProducts, FALSE_SITEWIDE);
  }

  public static SiteWideSale generateInvalidSitewideSale() {
    return new SiteWideSale(INVALID_ID, INVALID_NAME, VALID_TYPE, VALID_RATE, VALID_START_DATE,
        VALID_END_DATE, VALID_CREATION_DATE, VALID_ACTIVE, discountedProducts, FALSE_SITEWIDE);
  }

  public static SiteWideSale generateValidInactiveSitewideSale() {
    return new SiteWideSale(VALID_SECOND_ID, VALID_NAME, VALID_TYPE, VALID_RATE, VALID_START_DATE,
        VALID_END_DATE, VALID_CREATION_DATE, VALID_INACTIVE, discountedProducts, FALSE_SITEWIDE);
  }

  public static DiscountedProducts generateValidDiscountProducts() {
    return new DiscountedProducts(null, VALID_ID, VALID_ID);
  }

  public static DiscountedProducts generateInvalidDiscountProducts() {
    return new DiscountedProducts(null, VALID_ID, VALID_ID);
  }

  public static SiteWideSale generateInactiveSitewideSaleStartToday() {
    return new SiteWideSale(VALID_ID, VALID_NAME, VALID_TYPE, VALID_RATE, VALID_START_DATE_TODAY,
        VALID_END_DATE, VALID_CREATION_DATE, VALID_INACTIVE, discountedProducts, FALSE_SITEWIDE);
  }
}
