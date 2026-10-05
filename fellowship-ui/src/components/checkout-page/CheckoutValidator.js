/**
 * Validates the billing/delivery address and credit card information while also returning
 * errorMessages to be displayed
 * @param {*} billingData Data that was inputed in the billing input boxes
 * @param {*} deliveryData Data that was inputed in the delivery input boxes
 * @param {*} sameBillingAddressChecked Says whether the checkbox is checked if billing
 * information is the same as the delivery
 * @returns errorMessages — returns errors, if any
 */
const getCheckoutErrorMessages = (billingData, deliveryData, sameBillingAddressChecked) => {
  const errorMessages = {};
  const validZip = /(^\d{5}$)|(^\d{5}-\d{4}$)/;
  const validPhoneNumber = /^[1-9]\d{2}-\d{3}-\d{4}$/;
  const hasNumbers = /\d/;
  const validNameWithSpaces = /^((\s+)?\S+\s\S+\s\S+(\s+)?)$|^((\s+)?\S+\s\S+(\s+)?)$/;
  const validCardHolderName = /^(\S+\s\S+\s\S+)$|^(\S+\s\S+)$/;
  const validEmail = /^\w+([.-]?\w+)*@\w+([.-]?\w+)*(\.\w{2,3})+$/;
  const checkSpaces = /\s+/;

  if (sameBillingAddressChecked === false) {
    if (billingData.billingStreet.trim() === '') {
      errorMessages.billingStreet = 'This field is required.';
    } else if (billingData.billingStreet.trim().length > 60) {
      errorMessages.billingStreet = 'Enter valid street address.';
    }
    if (billingData.billingStreet2.trim().length > 60) {
      errorMessages.billingStreet2 = 'Exceeds maximum character limit of 60.';
    }
    if (billingData.billingCity.trim() === '') {
      errorMessages.billingCity = 'This field is required.';
    } else if (billingData.billingCity.length > 40) {
      errorMessages.billingCity = 'Enter valid city.';
    }
    if (billingData.billingZip === '') {
      errorMessages.billingZip = 'This field is required.';
    } else if (!validZip.test(billingData.billingZip)) {
      errorMessages.billingZip = 'Enter valid zip code in the format "12345" or "12345-6789".';
    }
    if (billingData.billingState === 'Select state' || billingData.billingState === null) {
      errorMessages.billingState = 'This field is required.';
    }
  }

  if (billingData.email.trim() === '') {
    errorMessages.email = 'This field is required.';
  } else if (billingData.email.trim().length > 40
            || billingData.email.trim().length === 0
            || !validEmail.exec(billingData.email.trim())) {
    errorMessages.email = 'Enter valid email address.';
  }

  if (billingData.phone === '') {
    errorMessages.phone = 'This field is required.';
  } else if (checkSpaces.test(billingData.phone)) {
    errorMessages.phone = 'Cannot contain any whitespace.';
  } else if (!validPhoneNumber.exec(billingData.phone)) {
    errorMessages.phone = 'Enter valid phone number in the format "123-456-7890".';
  }

  if (deliveryData.firstName.trim() === '' && deliveryData.firstName.trim().length === 0) {
    errorMessages.firstName = 'This field is required.';
  } else if (deliveryData.firstName.trim().length > 20 || hasNumbers.exec(deliveryData.firstName)) {
    errorMessages.firstName = 'Enter valid first name.';
  }
  if (deliveryData.lastName.trim() === '' && deliveryData.lastName.trim().length === 0) {
    errorMessages.lastName = 'This field is required.';
  } else if (deliveryData.lastName.trim().length > 20 || hasNumbers.exec(deliveryData.lastName)) {
    errorMessages.lastName = 'Enter valid last name.';
  }
  if (deliveryData.deliveryStreet.trim() === '') {
    errorMessages.deliveryStreet = 'This field is required.';
  } else if (deliveryData.deliveryStreet.trim().length > 60) {
    errorMessages.deliveryStreet = 'Enter valid street address.';
  }
  if (deliveryData.deliveryStreet2.trim().length > 60) {
    errorMessages.deliveryStreet2 = 'Exceeds maximum character limit of 60.';
  }
  if (deliveryData.deliveryCity.trim() === '') {
    errorMessages.deliveryCity = 'This field is required.';
  } else if (deliveryData.deliveryCity.trim().length > 40) {
    errorMessages.deliveryCity = 'Enter valid city.';
  }
  if (deliveryData.deliveryZip === '') {
    errorMessages.deliveryZip = 'This field is required.';
  } else if (!validZip.test(deliveryData.deliveryZip)) {
    errorMessages.deliveryZip = 'Enter valid zip code in the format "12345" or "12345-6789".';
  }
  if (deliveryData.deliveryState === 'Select state' || deliveryData.deliveryState === null) {
    errorMessages.deliveryState = 'This field is required.';
  }

  const validCardNumber = /^(51|52|53|54|55)[0-9]{14}$|^(4)[0-9]{15}$/;

  if (billingData.creditCard === null || billingData.creditCard === '') {
    errorMessages.cardNumber = 'This field is required.';
  } else if (checkSpaces.test(billingData.creditCard)) {
    errorMessages.cardNumber = 'Cannot contain any whitespace.';
  } else if (billingData.creditCard.length !== 16
    || !validCardNumber.exec(billingData.creditCard)) {
    errorMessages.cardNumber = 'Enter a valid Visa or MasterCard card number.';
  }

  const validCVV = /^[0-9]{3}$/;

  if (billingData.cvv === null || billingData.cvv.trim() === '') {
    errorMessages.cvv = 'This field is required.';
  } else if (checkSpaces.test(billingData.cvv)) {
    errorMessages.cvv = 'Cannot contain any whitespace.';
  } else if (!validCVV.exec(billingData.cvv)) {
    errorMessages.cvv = 'Enter a valid CVV in the format "123".';
  }

  /**
   * Validates the expiration date making sure the date matches the correct format
   * as well as making sure expiration date entered is not past the current date.
   * @param {*} expirationInputed Date that was inputed as the expiration for the credit card
   * @returns Boolean of whether the expiration is valid or not
   */
  const checkExpiration = (expirationInputed) => {
    const today = new Date();
    const currentMonth = Number(String(today.getMonth() + 1).padStart(2, '0'));
    const currentYear = Number(today.getFullYear().toString());
    const expirationArr = expirationInputed.toString().split('/');
    const expMonth = parseInt(expirationArr[0], 10);
    const expYear = parseInt(expirationArr[1], 10) + 2000;

    return (expYear < currentYear)
      || (expYear === currentYear && expMonth < currentMonth);
  };

  const validExpDateFormat = /^(0[1-9]|1[0-2])\/[0-9]{2}$/;

  if (billingData.expiration === null || billingData.expiration.trim() === '') {
    errorMessages.expiration = 'This field is required.';
  } else if (checkSpaces.test(billingData.expiration)) {
    errorMessages.expiration = 'Cannot contain any whitespace.';
  } else if (!validExpDateFormat.exec(billingData.expiration)) {
    errorMessages.expiration = 'Enter expiration date in the format of "mm/yy".';
  } else if (checkExpiration(billingData.expiration)) {
    errorMessages.expiration = 'You have entered an expiration date that has already passed. Please enter a valid expiration date.';
  }

  if (billingData.cardholder === null || billingData.cardholder.trim() === '') {
    errorMessages.cardholder = 'This field is required.';
  } else if (hasNumbers.exec(billingData.cardholder) || billingData.cardholder.length > 22) {
    errorMessages.cardholder = 'Enter a valid card holder name.';
  } else if (validNameWithSpaces.exec(billingData.cardholder)) {
    if (!validCardHolderName.exec(billingData.cardholder)) {
      errorMessages.cardholder = 'Cannot contain preceding and trailing whitespaces.';
    }
  } else {
    errorMessages.cardholder = 'Enter a valid card holder name.';
  }
  return errorMessages;
};

export default getCheckoutErrorMessages;
