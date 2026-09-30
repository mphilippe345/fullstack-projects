package com.superhero.superheroapi.domains.purchase;

import static com.superhero.superheroapi.constants.StringConstants.ALPHANUMERIC_REGEX;
import static com.superhero.superheroapi.constants.StringConstants.ALPHANUMERIC_VALIDATION_ERROR;
import static com.superhero.superheroapi.constants.StringConstants.EMAIL_REGEX;
import static com.superhero.superheroapi.constants.StringConstants.EMAIL_VALIDATION_ERROR;
import static com.superhero.superheroapi.constants.StringConstants.PHONE_REGEX;
import static com.superhero.superheroapi.constants.StringConstants.PHONE_VALIDATION_ERROR;
import static com.superhero.superheroapi.constants.StringConstants.REQUIRED_FIELD;

import jakarta.persistence.Embeddable;
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
public class PersonalInfo {

  @Pattern(regexp = ALPHANUMERIC_REGEX, message = "First name " + ALPHANUMERIC_VALIDATION_ERROR)
  @NotBlank(message = "First name " + REQUIRED_FIELD)
  private String firstName;

  @Pattern(regexp = ALPHANUMERIC_REGEX, message = "Last name " + ALPHANUMERIC_VALIDATION_ERROR)
  @NotBlank(message = "Last name " + REQUIRED_FIELD)
  private String lastName;

  @Pattern(regexp = EMAIL_REGEX, message = "Email " + EMAIL_VALIDATION_ERROR)
  @NotBlank(message = "Email " + REQUIRED_FIELD)
  private String email;

  @Pattern(regexp = PHONE_REGEX, message = "Phone number " + PHONE_VALIDATION_ERROR)
  @NotBlank(message = "Phone number " + REQUIRED_FIELD)
  private String phoneNumber;

}
