/**
 * Validates the new product information while also returning any errorMessages to be displayed
 * @param {*} newProductData Data that was inputed in the create-product-page input boxes
 * @returns errorMessages — returns errors, if any
 */

const getCreateProductErrorMessages = (newProductData) => {
  const errorMessages = {};
  const validAlphanumericWhitespace = /^[A-Za-z\d\s]*$/i;
  const validTwoDecimalPlaces = /^([0-9]+(\.?[0-9]?[0-9]?)?)$/;
  const validWholeNumber = /^[0-9]+$/;
  const validSku = /^([A-Za-z]{3}[-][A-Za-z]{3}[-][a-zA-Z]{2,4})$/;
  const validUrl = /(^(http:\/\/www\.|https:\/\/www\.|http:\/\/|https:\/\/)?[a-zA-Z0-9]+([\\-\\.]{1}[a-zA-Z0-9]+)*\.[a-zA-Z]{2,5}(:[0-9]{1,5})?(\/.*)?$)/i;
  const validHexCode = /^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$/i;
  const validStyleNumber = /^([A-Z]{1}[0-9]{1,5})$/i;
  const validGlobalProductCode = /^po-[0-9]{1,5}$/i;
  const validDate = /^(0[1-9]|1[012])[/.](0[1-9]|[12][0-9]|3[01])[/.]\d{4}$/;
  const validMaterial = /^[a-zA-Z]{0}[a-zA-Z\s]+$/;

  if (newProductData.active === '' && newProductData.active.trim().length === 0) {
    errorMessages.active = 'Please choose one.';
  }

  if (newProductData.name === '') {
    errorMessages.name = 'This field is required.';
  } else if (newProductData.name.length < 3) {
    errorMessages.name = 'Field must be more than 2 characters long.';
  } else if (newProductData.name.length > 100) {
    errorMessages.name = 'Max character limit of 100.';
  }

  if (newProductData.price === '') {
    errorMessages.price = 'This field is required.';
  } else if (newProductData.price < 0) {
    errorMessages.price = 'Price cannot be negative.';
  } else if (newProductData.price >= 1000000) {
    errorMessages.price = 'Price must be less than $1,000,000.';
  } else if (!validTwoDecimalPlaces.test(newProductData.price)) {
    errorMessages.price = 'Price can have no more than 2 decimal places.';
  }

  if (newProductData.quantity === '') {
    errorMessages.quantity = 'This field is required.';
  } else if (newProductData.quantity < 0) {
    errorMessages.quantity = 'Quantity must be greater than or equal to 0.';
  } else if (newProductData.quantity >= 1000000) {
    errorMessages.quantity = 'Quantity must be less than 1,000,000.';
  } else if (!validWholeNumber.test(newProductData.quantity)) {
    errorMessages.quantity = 'Quantity only accepts whole numbers.';
  }

  if (newProductData.sku === null || newProductData.sku.trim() === '') {
    errorMessages.sku = 'This field is required.';
  } else if (!validSku.exec(newProductData.sku)) {
    errorMessages.sku = 'SKU can accept alphabetic characters and hyphens only, e.g. "AAA-BBB-CCC."';
  }

  if (newProductData.description === '' || newProductData.description.trim().length === 0) {
    errorMessages.description = 'This field is required.';
  } else if (newProductData.description.length > 100) {
    errorMessages.description = 'Max character limit of 100.';
  }

  if (newProductData.demographic === 'Select one' || newProductData.demographic.trim().length === 0) {
    errorMessages.demographic = 'Please choose one.';
  }

  if (newProductData.category === '' || newProductData.category.trim().length === 0) {
    errorMessages.category = 'This field is required.';
  } else if (!validAlphanumericWhitespace.test(newProductData.category)) {
    errorMessages.category = 'No special characters allowed.';
  } else if (newProductData.category.length > 100) {
    errorMessages.category = 'Max character limit of 100.';
  }

  if (newProductData.type === '' || newProductData.type.trim().length === 0) {
    errorMessages.type = 'This field is required.';
  } else if (!validAlphanumericWhitespace.test(newProductData.type)) {
    errorMessages.type = 'No special characters allowed.';
  } else if (newProductData.type.length > 100) {
    errorMessages.type = 'Max character limit of 100.';
  }

  if (newProductData.brand === '' || newProductData.brand.trim().length === 0) {
    errorMessages.brand = 'This field is required.';
  } else if (newProductData.brand.length > 100) {
    errorMessages.brand = 'Max character limit of 100.';
  }

  if (newProductData.material === '' || newProductData.material.trim().length === 0) {
    errorMessages.material = 'This field is required.';
  } else if (newProductData.material.length > 100) {
    errorMessages.material = 'Max character limit of 100.';
  } else if (!validMaterial.test(newProductData.material)) {
    errorMessages.material = 'Characters must be alphabetical ';
  }

  if (newProductData.imageSrc === '' || newProductData.imageSrc.trim().length === 0) {
    errorMessages.imageSrc = 'This field is required.';
  } else if (!validUrl.test(newProductData.imageSrc)) {
    errorMessages.imageSrc = 'Enter a valid URL.';
  } else if (newProductData.imageSrc.length > 150) {
    errorMessages.imageSrc = 'Max character limit of 150.';
  }

  if (newProductData.primaryColorCode === '' || newProductData.primaryColorCode.trim().length === 0) {
    errorMessages.primaryColorCode = 'This field is required.';
  } else if (!validHexCode.test(newProductData.primaryColorCode)) {
    errorMessages.primaryColorCode = 'Enter a valid HEX code.';
  }

  if (newProductData.secondaryColorCode === '' || newProductData.secondaryColorCode.trim().length === 0) {
    errorMessages.secondaryColorCode = 'This field is required.';
  } else if (!validHexCode.test(newProductData.secondaryColorCode)) {
    errorMessages.secondaryColorCode = 'Enter a valid HEX code.';
  }

  if (newProductData.styleNumber === '' || newProductData.styleNumber.trim() === '') {
    errorMessages.styleNumber = 'This field is required.';
  } else if (!validStyleNumber.test(newProductData.styleNumber)) {
    errorMessages.styleNumber = 'Must begin with 1 letter, followed by 1-5 numbers';
  } else if (newProductData.styleNumber[0] !== newProductData.demographic[0]) {
    errorMessages.styleNumber = 'First letter of style number must be identical to first letter of demograpghic';
  }

  if (newProductData.globalProductCode === '' || newProductData.globalProductCode.trim().length === 0) {
    errorMessages.globalProductCode = 'This field is required.';
  } else if (!validGlobalProductCode.test(newProductData.globalProductCode)) {
    errorMessages.globalProductCode = 'Must begin with "po-", followed by 1 to 5 numbers.';
  }

  if (newProductData.releaseDate === '' || newProductData.releaseDate.trim().length === 0) {
    errorMessages.releaseDate = 'This field is required.';
  } else if (!validDate.test(newProductData.releaseDate)) {
    errorMessages.releaseDate = 'Valid date is required MM/DD/YYYY.';
  }

  return errorMessages;
};

export default getCreateProductErrorMessages;
