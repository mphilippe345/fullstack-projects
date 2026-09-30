package com.superhero.superheroapi.domains.purchase;

import static com.superhero.superheroapi.domains.product.ProductHelper.generateValidProductWithGenre1;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class PurchaseHelper {

  public static final String BASE_PURCHASES_PATH = "/api/purchases";

  public static final Long VALID_ID = 1L;
  public static final Long INVALID_ID = 0L;

  //Personal Info
  public static final String VALID_FIRST_NAME = "Bob";
  public static final String VALID_LAST_NAME = "Ross";
  public static final String VALID_EMAIL = "sross@catalyte.io";
  public static final String INVALID_EMAIL = "invalid@email.com";
  public static final String VALID_PHONE_NUMBER = "9999999999";

  //Address
  public static final String VALID_STREET_ADDRESS = "123 Main St";
  public static final String NULL_STREET_ADDRESS2 = null;
  public static final String VALID_CITY = "Baltimore";
  public static final String VALID_STATE = "MD";
  public static final String VALID_ZIPCODE = "21202";

  //Credit Card
  public static final String VALID_CARD_NAME = "Bob Ross";
  public static final String VALID_CARD_TYPE = "Visa";
  public static final String VALID_CARD_NUMBER = "4234567890123456";
  public static final String INVALID_CARD_NUMBER = "823256120265";
  public static final String VALID_EXPIRATION_DATE = "05/28";
  public static final String VALID_SECURITY_CODE = "123";

  //Line Item
  public static final String VALID_CONDITION = "new";
  public static final int VALID_QUANTITY = 1;
  public static final LocalDate VALID_LOCALDATE = LocalDate.now();
  public static final Date VALID_DATE = new Date();
  public static final Set<LineItem> VALID_LINEITEM_SET = new HashSet<>();

  public static final Set<LineItem> REQUEST_LINEITEM_SET = new HashSet<>();
  public static final Set<LineItem> NULL_LINEITEM_SET = null;

  public static final BigDecimal VALID_ORDER_TOTAL = BigDecimal.valueOf(28.45);
  public static final BigDecimal NULL_ORDER_TOTAL = null;
  public static final Purchase NEW_PURCHASE = new Purchase();

  // MISC
  public static final String VALID_ORDER_NUMBER = "COMIC-9DJ4NVNK34J";
  public static final int PAGE_NUMBER = 0;
  public static final int PAGE_SIZE = 2;

  public static final OrderStatus VALID_ORDER_STATUS = OrderStatus.PROCESSING_PAYMENT;

  public static PersonalInfo generateValidPersonalInfo() {
    return new PersonalInfo(VALID_FIRST_NAME, VALID_LAST_NAME, VALID_EMAIL, VALID_PHONE_NUMBER);
  }

  public static Address generateValidBillingAddress() {
    return new Address(VALID_STREET_ADDRESS, NULL_STREET_ADDRESS2, VALID_CITY, VALID_STATE,
        VALID_ZIPCODE);
  }

  public static Address generateValidShippingAddress() {
    return new Address(VALID_STREET_ADDRESS, NULL_STREET_ADDRESS2, VALID_CITY, VALID_STATE,
        VALID_ZIPCODE);
  }

  public static CreditCard generateValidCreditCard() {
    return new CreditCard(VALID_CARD_NAME, VALID_CARD_TYPE, VALID_CARD_NUMBER,
        VALID_EXPIRATION_DATE, VALID_SECURITY_CODE);
  }

  public static CreditCard generateInvalidCreditCard() {
    return new CreditCard(VALID_CARD_NAME, VALID_CARD_TYPE, INVALID_CARD_NUMBER,
        VALID_EXPIRATION_DATE, VALID_SECURITY_CODE);
  }

  public static LineItem generateValidLineItem() {
    return new LineItem(VALID_ID, NEW_PURCHASE, generateValidProductWithGenre1(), VALID_CONDITION,
        VALID_QUANTITY, VALID_LOCALDATE, VALID_DATE);
  }

  public static LineItem generateRequestLineItem() {
    LineItem lineItem = new LineItem();
    lineItem.setProduct(generateValidProductWithGenre1());
    lineItem.setCondition("new");
    lineItem.setQuantity(1);
    lineItem.setDate(LocalDate.now());
    return lineItem;
  }

  public static Purchase generateValidPurchase() {
    VALID_LINEITEM_SET.add(generateValidLineItem());
    return new Purchase(VALID_ID, VALID_LINEITEM_SET, generateValidPersonalInfo(),
        generateValidShippingAddress(), generateValidBillingAddress(), generateValidCreditCard(),
        VALID_ORDER_TOTAL, VALID_ORDER_NUMBER, VALID_LOCALDATE, VALID_ORDER_STATUS);
  }

  public static PurchaseDto generateValidPurchaseResponse(Purchase validPurchase) {
    PurchaseDto purchase = new PurchaseDto();
    purchase.setFirstName(validPurchase.getPersonalInfo().getFirstName());
    purchase.setEmail(validPurchase.getPersonalInfo().getEmail());
    purchase.setOrderNumber(validPurchase.getOrderNumber());
    purchase.setOrderTotal(validPurchase.getOrderTotal());
    purchase.setDate(validPurchase.getDate());
    purchase.setOrderStatus(validPurchase.getOrderStatus());
    return purchase;
  }

  public static Purchase generatePurchaseNullTotal() {
    return new Purchase(VALID_ID, NULL_LINEITEM_SET, generateValidPersonalInfo(),
        generateValidShippingAddress(), generateValidBillingAddress(), generateValidCreditCard(),
        NULL_ORDER_TOTAL, VALID_ORDER_NUMBER, VALID_LOCALDATE, VALID_ORDER_STATUS);
  }

  public static Purchase generateValidPurchaseController() {
    REQUEST_LINEITEM_SET.add(generateRequestLineItem());
    return new Purchase(VALID_ID, REQUEST_LINEITEM_SET, generateValidPersonalInfo(),
        generateValidShippingAddress(), generateValidBillingAddress(), generateValidCreditCard(),
        VALID_ORDER_TOTAL, VALID_ORDER_NUMBER, VALID_LOCALDATE, VALID_ORDER_STATUS);
  }

  public static Purchase generateInvalidPurchaseController() {
    REQUEST_LINEITEM_SET.add(generateRequestLineItem());
    return new Purchase(VALID_ID, REQUEST_LINEITEM_SET, generateValidPersonalInfo(),
        generateValidShippingAddress(), generateValidBillingAddress(), generateInvalidCreditCard(),
        VALID_ORDER_TOTAL, VALID_ORDER_NUMBER, VALID_LOCALDATE, VALID_ORDER_STATUS);
  }

  public static Purchase generateInvalidPurchase() {
    VALID_LINEITEM_SET.add(generateValidLineItem());
    return new Purchase(VALID_ID, VALID_LINEITEM_SET, generateValidPersonalInfo(),
        generateValidShippingAddress(), generateValidBillingAddress(), generateValidCreditCard(),
        VALID_ORDER_TOTAL, VALID_ORDER_NUMBER, VALID_LOCALDATE, VALID_ORDER_STATUS);
  }

  public static Purchase generateInvalidPurchaseNullLineItems() {
    return new Purchase(INVALID_ID, NULL_LINEITEM_SET, generateValidPersonalInfo(),
        generateValidShippingAddress(), generateValidBillingAddress(), generateValidCreditCard(),
        VALID_ORDER_TOTAL, VALID_ORDER_NUMBER, VALID_LOCALDATE, VALID_ORDER_STATUS);
  }

  public static Purchase generateValidPurchaseSet() {
    Purchase validPurchase = new Purchase();
    LineItem validLineItem = new LineItem();
    Set<LineItem> validLineItemSet = new HashSet<>();

    validPurchase.setId(VALID_ID);
    validPurchase.setPersonalInfo(generateValidPersonalInfo());
    validPurchase.setBillingAddress(generateValidBillingAddress());
    validPurchase.setCreditCard(generateValidCreditCard());
    validPurchase.setOrderTotal(VALID_ORDER_TOTAL);
    validPurchase.setShippingAddress(generateValidShippingAddress());
    validLineItem.setProduct(generateValidProductWithGenre1());
    validLineItem.setQuantity(VALID_QUANTITY);
    validLineItemSet.add(validLineItem);
    validPurchase.setProducts(validLineItemSet);

    return validPurchase;
  }

  public static Purchase generateNullPurchase() {
    return new Purchase();
  }

}
