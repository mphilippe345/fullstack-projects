export const formatGiftCard = (
    giftCardCode: string,
    masked: boolean = false
  ) => {
    let code = giftCardCode.replace(/[^A-HJ-NP-Z2-9]/g, '');
  
    const format: { [key: number]: string } = {
      4: '-',
      8: '-',
    };

    let formattedCode: string = '';
    let maxDigits = 12;
  
    code = code.slice(0, maxDigits);
  
    for (let i = 0; i < code.length; i++) {
      formattedCode += (format[i] || '') + code[i];
    }
  
    if (masked) {
      let maskOffset = 4;
      let maskedCode = '';
      for (let i = 0; i < formattedCode.length; i++) {
        if (
          formattedCode[i] != ' ' &&
          i < formattedCode.length - maskOffset
        ) {
          maskedCode += '*';
        } else {
          maskedCode += formattedCode[i];
        }
      }
  
      formattedCode = maskedCode;
    }
  
    return formattedCode;
  };