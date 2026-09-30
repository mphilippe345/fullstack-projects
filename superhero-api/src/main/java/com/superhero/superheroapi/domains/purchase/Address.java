package com.superhero.superheroapi.domains.purchase;

import static com.superhero.superheroapi.constants.StringConstants.ALPHANUMERIC_REGEX;
import static com.superhero.superheroapi.constants.StringConstants.ALPHANUMERIC_VALIDATION_ERROR;
import static com.superhero.superheroapi.constants.StringConstants.REQUIRED_FIELD;
import static com.superhero.superheroapi.constants.StringConstants.STATE_REGEX;
import static com.superhero.superheroapi.constants.StringConstants.STATE_VALIDATION_ERROR;
import static com.superhero.superheroapi.constants.StringConstants.ZIPCODE_REGEX;
import static com.superhero.superheroapi.constants.StringConstants.ZIPCODE_VALIDATION_ERROR;

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
public class Address {

    @Pattern(regexp = ALPHANUMERIC_REGEX, message = "Primary street address " + ALPHANUMERIC_VALIDATION_ERROR)
    @NotBlank(message = "Primary street address " + REQUIRED_FIELD)
    private String streetAddress1;

    @Pattern(regexp = ALPHANUMERIC_REGEX, message = "Secondary street address " + ALPHANUMERIC_VALIDATION_ERROR)
    private String streetAddress2;

    @Pattern(regexp = ALPHANUMERIC_REGEX, message = "City " + ALPHANUMERIC_VALIDATION_ERROR)
    @NotBlank(message = "City " + REQUIRED_FIELD)
    private String city;

    @Pattern(regexp = STATE_REGEX, message = "State " + STATE_VALIDATION_ERROR)
    @NotBlank(message = "State " + REQUIRED_FIELD)
    private String state;

    @Pattern(regexp = ZIPCODE_REGEX, message = "Zip code " + ZIPCODE_VALIDATION_ERROR)
    @NotBlank(message = "Zip code " + REQUIRED_FIELD)
    private String zipCode;

}
