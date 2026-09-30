export const formatPhoneNumber = (phoneNumber: string, isDisplay: boolean = false) => {
  const numbers = phoneNumber.replace(/\D/g, '');
  const char: { [key: number]: string } = { 0: '(', 3: ') ', 6: '-' };
  let formattedNumber = '';

  for (let i = 0; i < numbers.length && i < 10; i++) {
    formattedNumber += (char[i] || '') + numbers[i];
  }

  if (isDisplay) {
    formattedNumber = formattedNumber.replace('-', ' - ');
  }

  return formattedNumber;
};
