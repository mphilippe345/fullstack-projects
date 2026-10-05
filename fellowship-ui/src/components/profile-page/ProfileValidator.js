/**
 * validates if the information that a user input is valid for updating the profile information
 * @param {string} user- user profile
 * @returns errors if any
 */
const ProfileValidator = (user) => {
  const errorMessages = {};
  const validAlphaNumeric = /^[a-z0-9]*$/i;
  const validSpecialCharacters = /\W|_/;

  if (user.firstName.trim().length > 20 || user.firstName === '' || user.firstName === null) {
    errorMessages.firstName = 'Invalid first name.';
  } else if (!validAlphaNumeric.test(user.firstName)
  || (validSpecialCharacters.test(user.firstName))) {
    errorMessages.firstName = 'Invalid first name.';
  }
  if (user.lastName.trim().length > 20 || user.lastName === '' || user.lastName === null) {
    errorMessages.lastName = 'Invalid last name.';
  } else if (!validAlphaNumeric.test(user.lastName)
  || (validSpecialCharacters.test(user.lastName))) {
    errorMessages.lastName = 'Invalid last name.';
  }
  return errorMessages;
};

export default ProfileValidator;
