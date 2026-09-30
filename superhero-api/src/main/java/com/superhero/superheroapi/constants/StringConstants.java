package com.superhero.superheroapi.constants;

public class StringConstants {

  public static final String DATA_ACCESS_ERROR = "An error occurred when attempting to access the data requested.";

  public static final String REQUIRED_FIELD = "is a required field";

  public static final String ALPHANUMERIC_REGEX = "(^[A-Za-z0-9`~!@#$%^&*_=+;:'?><,. -]+$)|(^$)";
  public static final String ALPHANUMERIC_VALIDATION_ERROR = "must have only alphanumeric or certain special characters";

  public static final String EMAIL_REGEX = "^[-a-zA-Z0-9]+@[a-zA-Z]+\\.[a-zA-Z]+$";
  public static final String EMAIL_VALIDATION_ERROR = "must be formatted as x@x.x";

  public static final String PHONE_REGEX = "^\\d{10}$";
  public static final String PHONE_VALIDATION_ERROR = "must have exactly 10 digits";

  public static final String STATE_REGEX = "^(A[LKZR]|C[AOT]|D[EC]|FL|GA|HI|I[ADLN]|K[SY]|LA|M[ADEINOST]|N[CDEHJMVY]|O[HKR]|PA|RI|S[CD]|T[NX]|UT|V[AT]|W[AIVY])$";
  public static final String STATE_VALIDATION_ERROR = "must be a two character state code";

  public static final String ZIPCODE_REGEX = "^\\d{5}(?:[-]\\d{4})?$";
  public static final String ZIPCODE_VALIDATION_ERROR = "must be formatted as xxxxx or xxxxx-xxxx";

  public static final String CARDTYPE_REGEX = "^(?:Visa|Mastercard|Discover|American Express)$";
  public static final String CARDTYPE_VALIDATION_ERROR = "must be Visa, Mastercard, Discover, or American Express";

  public static final String CARDNUMBER_REGEX = "^[3-6]\\d{14}$|^[3-6]\\d{15}$";
  public static final String CARDNUMBER_VALIDATION_ERROR = "must have exactly 15 or 16 digits";

  public static final String EXPIRATIONDATE_REGEX = "^(0[1-9]|1[0-2])\\/?([0-9]{2})$";
  public static final String EXPIRATIONDATE_VALIDATION_ERROR = "must have month and year as MM/yy";

  public static final String SECURITYCODE_REGEX = "^\\d{3}$|^\\d{4}$";
  public static final String SECURITYCODE_VALIDATION_ERROR = "must have exactly 3 or 4 digits";

  public static final String LIKE_NEW_DISCOUNT = "0.95";

  public static final String VERY_GOOD_DISCOUNT = "0.9";

  public static final String GOOD_DISCOUNT = "0.75";

  public static final String ACCEPTABLE_DISCOUNT = "0.6";

  public static final char[] CHAR_SET = "0123456789abcdefghijklmnopqrstuvwxyz".toCharArray();

}
