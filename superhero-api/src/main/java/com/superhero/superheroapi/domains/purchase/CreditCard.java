package com.superhero.superheroapi.domains.purchase;

import static com.superhero.superheroapi.constants.StringConstants.ALPHANUMERIC_REGEX;
import static com.superhero.superheroapi.constants.StringConstants.ALPHANUMERIC_VALIDATION_ERROR;
import static com.superhero.superheroapi.constants.StringConstants.CARDNUMBER_REGEX;
import static com.superhero.superheroapi.constants.StringConstants.CARDNUMBER_VALIDATION_ERROR;
import static com.superhero.superheroapi.constants.StringConstants.CARDTYPE_REGEX;
import static com.superhero.superheroapi.constants.StringConstants.CARDTYPE_VALIDATION_ERROR;
import static com.superhero.superheroapi.constants.StringConstants.EXPIRATIONDATE_REGEX;
import static com.superhero.superheroapi.constants.StringConstants.EXPIRATIONDATE_VALIDATION_ERROR;
import static com.superhero.superheroapi.constants.StringConstants.REQUIRED_FIELD;
import static com.superhero.superheroapi.constants.StringConstants.SECURITYCODE_REGEX;
import static com.superhero.superheroapi.constants.StringConstants.SECURITYCODE_VALIDATION_ERROR;

import jakarta.persistence.Embeddable;
import java.time.LocalDate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class CreditCard {

  @Pattern(regexp = ALPHANUMERIC_REGEX, message = "Name " + ALPHANUMERIC_VALIDATION_ERROR)
  @NotBlank(message = "Name " + REQUIRED_FIELD)
  private String cardName;

  @Pattern(regexp = CARDTYPE_REGEX, message = "Card type " + CARDTYPE_VALIDATION_ERROR)
  @NotBlank(message = "Card type " + REQUIRED_FIELD)
  private String cardType;

  @Pattern(regexp = CARDNUMBER_REGEX, message = "Card number " + CARDNUMBER_VALIDATION_ERROR)
  @NotBlank(message = "Card number " + REQUIRED_FIELD)
  private String cardNumber;

  @Pattern(regexp = EXPIRATIONDATE_REGEX, message = "Expiration date" + EXPIRATIONDATE_VALIDATION_ERROR)
  @NotBlank(message = "Expiration date " + REQUIRED_FIELD)
  private String expirationDate;

  @Pattern(regexp = SECURITYCODE_REGEX, message = "Security code" + SECURITYCODE_VALIDATION_ERROR)
  @NotBlank(message = "Security code " + REQUIRED_FIELD)
  private String securityCode;

}
