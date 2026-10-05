using Catalyte.Apparel.Data.Models;
using System;
using System.Collections.Generic;
using Xunit;
using Catalyte.Apparel.Providers;
using System.Linq;
namespace Catalyte.Apparel.Test.Unit
{
    public class CreditCardValidatorUnitTests
    {

        [Fact]
        public void CardHolder_Is_Empty_Returns_Throws_Correct_Exception_Message()
        {
            var exceptions = new List<Exception>();
            var purchase = new Purchase()
            {
                CardHolder = "",
                CardNumber = "1435678998761234",
                Expiration = "11/21",
                CVV = "456",
            };
            Assert.Equal("Enter a valid card holder name", CreditCardValidator.ValidateCardholder(purchase, exceptions)[0].Message);
        }



        [Fact]
        public void CardHolder_Is_Null_Throws_Correct_Exception_Message()
        {
            var exceptions = new List<Exception>();
            var purchase = new Purchase()
            {
                CardHolder = null,
                CardNumber = "1435678998761234",
                Expiration = "11/21",
                CVV = "456",
            };
            Assert.Equal("Card holder name can not be null", CreditCardValidator.ValidateCardholder(purchase, exceptions)[0].Message);
        }



        [Fact]
        public void CardHolder_With_Two_Names_Does_Not_Throw_Exception()
        {
            var exceptions = new List<Exception>();
            var purchase = new Purchase()
            {
                CardHolder = "Max Space",
                CardNumber = "1435678998761234",
                Expiration = "11/21",
                CVV = "456",
            };
            Assert.False(CreditCardValidator.ValidateCardholder(purchase, exceptions).Any());
        }



        [Fact]
        public void CardHolder_With_Two_Names_And_Whitespace_Does_Not_Throw_Exception()
        {
            var exceptions = new List<Exception>();
            var purchase = new Purchase()
            {
                CardHolder = " Max  Space   ",
                CardNumber = "1435678998761234",
                Expiration = "11/21",
                CVV = "456",
            };
            Assert.False(CreditCardValidator.ValidateCardholder(purchase, exceptions).Any());
        }



        [Fact]
        public void CardHolder_With_Two_Names_And_Middle_Initial_Does_Not_Throw_Exception()
        {
            var exceptions = new List<Exception>();
            var purchase = new Purchase()
            {
                CardHolder = "Max I Space",
                CardNumber = "1435678998761234",
                Expiration = "11/21",
                CVV = "456",
            };
            Assert.False(CreditCardValidator.ValidateCardholder(purchase, exceptions).Any());
        }



        [Fact]
        public void CardHolder_With_Two_Names_And_Number_Throws_Correct_Exception_Message()
        {
            var exceptions = new List<Exception>();
            var purchase = new Purchase()
            {
                CardHolder = "Max 8 Space",
                CardNumber = "1435678998761234",
                Expiration = "11/21",
                CVV = "456",
            };
            Assert.Equal("Enter a valid card holder name", CreditCardValidator.ValidateCardholder(purchase, exceptions)[0].Message);
        }



        [Fact]
        public void CardHolder_With_Two_Names_And_Special_Character_Does_Not_Throw_Exception()
        {
            var exceptions = new List<Exception>();
            var purchase = new Purchase()
            {
                CardHolder = "Max & Spa&ce ",
                CardNumber = "1435678998761234",
                Expiration = "11/21",
                CVV = "456",
            };
            Assert.False(CreditCardValidator.ValidateCardholder(purchase, exceptions).Any());
        }



        [Fact]
        public void CardHolder_With_Two_Names_Middle_Initial_And_Whitespace_Does_Not_Throw_Exception()
        {
            var exceptions = new List<Exception>();
            var purchase = new Purchase()
            {
                CardHolder = " Max I  Space  ",
                CardNumber = "1435678998761234",
                Expiration = "11/21",
                CVV = "456",
            };
            Assert.False(CreditCardValidator.ValidateCardholder(purchase, exceptions).Any());
        }



        [Fact]
        public void CardHolder_With_One_Name_Throws_Correct_Exception_Message()
        {
            var exceptions = new List<Exception>();
            var purchase = new Purchase()
            {
                CardHolder = "Max ",
                CardNumber = "1435678998761234",
                Expiration = "11/21",
                CVV = "456",
            };
            Assert.Equal("Enter a valid card holder name", CreditCardValidator.ValidateCardholder(purchase, exceptions)[0].Message);
        }

        [Fact]
        public void CardHolder_With_Four_Names_Throws_Correct_Exception_Message()
        {
            var exceptions = new List<Exception>();
            var purchase = new Purchase()
            {
                CardHolder = " Max Italic Line Space",
                CardNumber = "1435678998761234",
                Expiration = "11/21",
                CVV = "456",
            };
            Assert.Equal("Enter a valid card holder name", CreditCardValidator.ValidateCardholder(purchase, exceptions)[0].Message);
        }



        [Fact]
        public void CVV_Three_Integer_Does_Not_Throw_Exception()
        {
            var exceptions = new List<Exception>();
            var purchase = new Purchase()
            {
                CardHolder = "Max Space",
                CardNumber = "1435678998761234",
                Expiration = "11/21",
                CVV = "456",
            };
            Assert.False(CreditCardValidator.ValidateCVV(purchase, exceptions).Any());
        }



        [Fact]
        public void CVV_Four_Integer_Throws_Correct_Exception_Message()
        {
            var exceptions = new List<Exception>();
            var purchase = new Purchase()
            {
                CardHolder = "Max Space",
                CardNumber = "1435678998761234",
                Expiration = "11/21",
                CVV = "4566",
            };
            Assert.Equal("Enter a valid CVV", CreditCardValidator.ValidateCVV(purchase, exceptions)[0].Message);
        }



        [Fact]
        public void CVV_Two_Integer_Throws_Correct_Exception_Message()
        {
            var exceptions = new List<Exception>();
            var purchase = new Purchase()
            {
                CardHolder = "Max Space",
                CardNumber = "1435678998761234",
                Expiration = "11/21",
                CVV = "45",
            };
            Assert.Equal("Enter a valid CVV", CreditCardValidator.ValidateCVV(purchase, exceptions)[0].Message);
        }



        [Fact]
        public void CVV_One_Integer_Throws_Correct_Exception_Message()
        {
            var exceptions = new List<Exception>();
            var purchase = new Purchase()
            {
                CardHolder = "Max Space",
                CardNumber = "1435678998761234",
                Expiration = "11/21",
                CVV = "4",
            };
            Assert.Equal("Enter a valid CVV", CreditCardValidator.ValidateCVV(purchase, exceptions)[0].Message);
        }



        [Fact]
        public void Card_Number_Null_Throws_Correct_Exception_Message()
        {
            var exceptions = new List<Exception>();
            var purchase = new Purchase()
            {
                CardHolder = "Max Space",
                CardNumber = null,
                Expiration = "11/21",
                CVV = "456",
            };
            Assert.Equal("Card number can not be null", CreditCardValidator.ValidateCardNumber(purchase, exceptions)[0].Message);
        }



        [Fact]
        public void Card_Number_Empty_Throws_Correct_Exception_Message()
        {
            var exceptions = new List<Exception>();
            var purchase = new Purchase()
            {
                CardHolder = "Max Space",
                CardNumber = "",
                Expiration = "11/21",
                CVV = "456",
            };
            Assert.Equal("Enter a valid Visa or MasterCard card number", CreditCardValidator.ValidateCardNumber(purchase, exceptions)[0].Message);
        }



        [Fact]
        public void Card_Number_Whitespace_Throws_Correct_Exception_Message()
        {
            var exceptions = new List<Exception>();
            var purchase = new Purchase()
            {
                CardHolder = "Max Space",
                CardNumber = " ",
                Expiration = "11/21",
                CVV = "456",
            };
            Assert.Equal("Enter a valid Visa or MasterCard card number", CreditCardValidator.ValidateCardNumber(purchase, exceptions)[0].Message);
        }



        [Fact]
        public void Card_Number_17_Numbers_Throws_Correct_Exception_Message()
        {
            var exceptions = new List<Exception>();
            var purchase = new Purchase()
            {
                CardHolder = "Max Space",
                CardNumber = "12345678912345678",
                Expiration = "11/21",
                CVV = "456",
            };
            Assert.Equal("Enter a valid Visa or MasterCard card number", CreditCardValidator.ValidateCardNumber(purchase, exceptions)[0].Message);
        }



        [Fact]
        public void Card_Number_15_Numbers_Throws_Correct_Exception_Message()
        {
            var exceptions = new List<Exception>();
            var purchase = new Purchase()
            {
                CardHolder = "Max Space",
                CardNumber = "123456789123456",
                Expiration = "11/21",
                CVV = "456",
            };
            Assert.Equal("Enter a valid Visa or MasterCard card number", CreditCardValidator.ValidateCardNumber(purchase, exceptions)[0].Message);
        }



        [Fact]
        public void Card_Number_16_Numbers_Throws_Correct_Exception_Message()
        {
            var exceptions = new List<Exception>();
            var purchase = new Purchase()
            {
                CardHolder = "Max Space",
                CardNumber = "1234567891234567",
                Expiration = "11/21",
                CVV = "456",
            };
            Assert.Equal("Enter a valid Visa or MasterCard card number", CreditCardValidator.ValidateCardNumber(purchase, exceptions)[0].Message);
        }



        [Fact]
        public void Card_Number_Valid_Visa_But_17_Numbers_And_Throws_Correct_Exception_Message()
        {
            var exceptions = new List<Exception>();
            var purchase = new Purchase()
            {
                CardHolder = "Max Space",
                CardNumber = "42345678912345678",
                Expiration = "11/21",
                CVV = "456",
            };
            Assert.Equal("Enter a valid Visa or MasterCard card number", CreditCardValidator.ValidateCardNumber(purchase, exceptions)[0].Message);
        }



        [Fact]
        public void Card_Number_Valid_Visa_But_15_Numbers_Throws_Correct_Exception_Message()
        {
            var exceptions = new List<Exception>();
            var purchase = new Purchase()
            {
                CardHolder = "Max Space",
                CardNumber = "423456789123456",
                Expiration = "11/21",
                CVV = "456",
            };
            Assert.Equal("Enter a valid Visa or MasterCard card number", CreditCardValidator.ValidateCardNumber(purchase, exceptions)[0].Message);
        }



        [Fact]
        public void Card_Number_Valid_Visa_And_Whitespace_Throws_Correct_Exception_Message()
        {
            var exceptions = new List<Exception>();
            var purchase = new Purchase()
            {
                CardHolder = "Max Space",
                CardNumber = "4234567891234567 ",
                Expiration = "11/21",
                CVV = "456",
            };
            Assert.Equal("Enter a valid Visa or MasterCard card number", CreditCardValidator.ValidateCardNumber(purchase, exceptions)[0].Message);
        }



        [Fact]
        public void Card_Number_Valid_Visa_Does_Not_Throw_Exception()
        {
            var exceptions = new List<Exception>();
            var purchase = new Purchase()
            {
                CardHolder = "Max Space",
                CardNumber = "4234567891234567",
                Expiration = "11/21",
                CVV = "456",
            };
            Assert.False(CreditCardValidator.ValidateCardNumber(purchase, exceptions).Any());
        }



        [Fact]
        public void Card_Number_Valid_Mastercard_Starts_With_51_Does_Not_Throw_Exception()
        {
            var exceptions = new List<Exception>();
            var purchase = new Purchase()
            {
                CardHolder = "Max Space",
                CardNumber = "5134567891234567",
                Expiration = "11/21",
                CVV = "456",
            };
            Assert.False(CreditCardValidator.ValidateCardNumber(purchase, exceptions).Any());
        }



        [Fact]
        public void Card_Number_Valid_Mastercard_Starts_With_52_Does_Not_Throw_Exception()
        {
            var exceptions = new List<Exception>();
            var purchase = new Purchase()
            {
                CardHolder = "Max Space",
                CardNumber = "5234567891234567",
                Expiration = "11/21",
                CVV = "456",
            };
            Assert.False(CreditCardValidator.ValidateCardNumber(purchase, exceptions).Any());
        }



        [Fact]
        public void Card_Number_Valid_Mastercard_Starts_With_53_Does_Not_Throw_Exception()
        {
            var exceptions = new List<Exception>();
            var purchase = new Purchase()
            {
                CardHolder = "Max Space",
                CardNumber = "5334567891234567",
                Expiration = "11/21",
                CVV = "456",
            };
            Assert.False(CreditCardValidator.ValidateCardNumber(purchase, exceptions).Any());
        }



        [Fact]
        public void Card_Number_Valid_Mastercard_Starts_With_54_Does_Not_Throw_Exception()
        {
            var exceptions = new List<Exception>();
            var purchase = new Purchase()
            {
                CardHolder = "Max Space",
                CardNumber = "5434567891234567",
                Expiration = "11/21",
                CVV = "456",
            };
            Assert.False(CreditCardValidator.ValidateCardNumber(purchase, exceptions).Any());
        }



        [Fact]
        public void Card_Number_Valid_Mastercard_Starts_With_55_Does_Not_Throw_Exception()
        {
            var exceptions = new List<Exception>();
            var purchase = new Purchase()
            {
                CardHolder = "Max Space",
                CardNumber = "5534567891234567",
                Expiration = "11/21",
                CVV = "456",
            };
            Assert.False(CreditCardValidator.ValidateCardNumber(purchase, exceptions).Any());
        }



        [Fact]
        public void Expiration_Null_Throws_Correct_Exception()
        {
            var exceptions = new List<Exception>();
            var purchase = new Purchase()
            {
                CardHolder = "Max Space",
                CardNumber = "5234567891234567",
                Expiration = null,
                CVV = "456",
            };
            Assert.Equal("Expiration can not be null", CreditCardValidator.ValidateExpiration(purchase, exceptions)[0].Message);
        }



        [Fact]
        public void Expiration_Empty_Throws_Correct_Exception()
        {
            var exceptions = new List<Exception>();
            var purchase = new Purchase()
            {
                CardHolder = "Max Space",
                CardNumber = "5234567891234567",
                Expiration = "",
                CVV = "456",
            };
            Assert.Equal("Enter a valid expiration date", CreditCardValidator.ValidateExpiration(purchase, exceptions)[0].Message);
        }



        [Fact]
        public void Expiration_In_Past_Throws_Correct_Exception()
        {
            var exceptions = new List<Exception>();
            var purchase = new Purchase()
            {
                CardHolder = "Max Space",
                CardNumber = "5234567891234567",
                Expiration = "11/21",
                CVV = "456",
            };
            Assert.Equal("Enter a valid expiration date", CreditCardValidator.ValidateExpiration(purchase, exceptions)[0].Message);
        }



        [Fact]
        public void Expiration_In_Future_Does_Not_Throw_Exception()
        {
            var exceptions = new List<Exception>();
            var purchase = new Purchase()
            {
                CardHolder = "Max Space",
                CardNumber = "5234567891234567",
                Expiration = "11/30",
                CVV = "456",
            };
            Assert.False(CreditCardValidator.ValidateExpiration(purchase, exceptions).Any());
        }

    }
}