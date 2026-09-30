package com.superhero.superheroapi.domains.product;

import com.superhero.superheroapi.domains.promo.PromoCode;
import com.superhero.superheroapi.domains.review.Review;

import com.superhero.superheroapi.domains.sitewidesales.SiteWideSale;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.modelmapper.ModelMapper;

public class ProductHelper {

    public static final String BASE_PRODUCTS_PATH = "/api/products";

    public static final Long VALID_ID = 1L;

    public static final Long INVALID_ID = 0L;
    public static final String VALID_TITLE = "Test Title";

    public static final String VALID_AUTHOR = "Test Author";
    public static final String VALID_DESCRIPTION = "Test Description";
    public static final String VALID_GENRE_1 = "Genre1";
    public static final String VALID_GENRE_2 = "Genre2";
    public static final String VALID_IMG_URL = "testimgurl.com";
    public static final Integer VALID_ISSUE = 1;
    public static final String VALID_VOLUME = "Test Volume";
    public static final Boolean NEW_RELEASE = true;
    public static final String VALID_PUBLISHER = "Test Publisher";
    public static final Boolean STOCK_STATUS = true;
    public static final Boolean ACTIVE = true;
    public static final String VALID_SKU = "SKU123";
    public static final LocalDate VALID_DATE = LocalDate.now();
    public static final BigDecimal VALID_PRICE = new BigDecimal("1");
    public static final Boolean BESTSELLER = true;
    public static final List<PromoCode> PROMO_CODE_LIST = new ArrayList<>();
    public static final List<Review> REVIEW_LIST = new ArrayList<>();

    public static final List<SiteWideSale> SITEWIDE_SALES = new ArrayList<>();
    public static final boolean BOOLEAN_TRUE = true;

    public static Product generateValidProductWithGenre1() {
        return new Product(VALID_ID, VALID_TITLE,VALID_AUTHOR, VALID_DESCRIPTION, VALID_GENRE_1, VALID_IMG_URL, VALID_ISSUE, VALID_VOLUME, NEW_RELEASE, VALID_PUBLISHER, STOCK_STATUS, ACTIVE, VALID_SKU, VALID_DATE, VALID_PRICE,BESTSELLER, REVIEW_LIST, PROMO_CODE_LIST, SITEWIDE_SALES);
    }

    public static Product generateValidProductWithGenre2() {
        return new Product(VALID_ID, VALID_TITLE,VALID_AUTHOR, VALID_DESCRIPTION, VALID_GENRE_2, VALID_IMG_URL, VALID_ISSUE, VALID_VOLUME, NEW_RELEASE, VALID_PUBLISHER, STOCK_STATUS, ACTIVE, VALID_SKU, VALID_DATE, VALID_PRICE,BESTSELLER, REVIEW_LIST, PROMO_CODE_LIST, SITEWIDE_SALES);
    }

    public static Product generateInvalidProductWithGenre2() {
        return new Product(INVALID_ID, VALID_TITLE,VALID_AUTHOR, VALID_DESCRIPTION, VALID_GENRE_2, VALID_IMG_URL, VALID_ISSUE, VALID_VOLUME, NEW_RELEASE, VALID_PUBLISHER, STOCK_STATUS, ACTIVE, VALID_SKU, VALID_DATE, VALID_PRICE,BESTSELLER, REVIEW_LIST, PROMO_CODE_LIST, SITEWIDE_SALES);

    }

    public static ProductModel generateValidProductModelWithGenre1() {
        ModelMapper mapper = new ModelMapper();
        return mapper.map(generateValidProductWithGenre1(), ProductModel.class);
    }

    public static ProductModel generateValidProductModel() {
        return new ProductModel(VALID_ID, VALID_TITLE, VALID_AUTHOR, VALID_DESCRIPTION, VALID_GENRE_1, VALID_IMG_URL, VALID_ISSUE, VALID_VOLUME, BOOLEAN_TRUE, VALID_PUBLISHER, BOOLEAN_TRUE, BOOLEAN_TRUE, VALID_SKU, VALID_DATE, VALID_PRICE, BOOLEAN_TRUE);
    }

    public static ProductStatusesModel generateValidProductStatusesModel() {
        return new ProductStatusesModel(generateValidProductWithGenre1().getActive(), generateValidProductWithGenre1().getStockStatus());
    }
}
