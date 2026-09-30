export const formatCreditCard = (
  creditCardNumber: string,
  masked: boolean = false
) => {
  let numbers = creditCardNumber.replace(/\D/g, '');

  const visaMasterDiscoverFormat: { [key: number]: string } = {
    4: ' ',
    8: ' ',
    12: ' ',
  };

  const amexFormat: { [key: number]: string } = { 4: ' ', 10: ' ' };
  let formattedNumber: string = '';
  let format = visaMasterDiscoverFormat;
  let maxDigits = 16;

  if (numbers.startsWith('3')) {
    // It's an Amex, use the Amex formatting.
    format = amexFormat;
    maxDigits = 15;
  }

  numbers = numbers.slice(0, maxDigits);

  for (let i = 0; i < numbers.length; i++) {
    formattedNumber += (format[i] || '') + numbers[i];
  }

  if (masked) {
    let maskOffset = numbers.length == 16 ? 4 : 5;
    let maskedNumbers = '';
    for (let i = 0; i < formattedNumber.length; i++) {
      if (
        formattedNumber[i] != ' ' &&
        i < formattedNumber.length - maskOffset
      ) {
        maskedNumbers += '*';
      } else {
        maskedNumbers += formattedNumber[i];
      }
    }

    formattedNumber = maskedNumbers;
  }

  return formattedNumber;
};
