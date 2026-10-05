using System;
using System.Collections.Generic;
using System.Text.RegularExpressions;
using Catalyte.Apparel.Data.Models;
namespace Catalyte.Apparel.Providers
{
    public class CreditCardValidator
    {
        /// <summary>
        /// Uses all credit card business validation helper methods
        /// </summary>
        /// <param name="purchase">The current purchase trying to persist to the database</param>
        /// <returns>A list of exceptions for validation errors, if any</returns>
        public static List<Exception> ValidateCreditCard(Purchase purchase)
        {
            var exceptions = new List<Exception>();
            ValidateCardholder(purchase, exceptions);
            ValidateCVV(purchase, exceptions);
            ValidateCardNumber(purchase, exceptions);
            ValidateExpiration(purchase, exceptions);
            return exceptions;
        }

        /// <summary>
        /// Validates the cardholder name within the purchase object
        /// </summary>
        /// <param name="purchase">A purchase object to be persisted to the database</param>
        /// <param name="exceptions">A list of exceptions</param>
        /// <returns>Returns the list of exceptions with an exception added if there is an error</returns>
        public static List<Exception> ValidateCardholder(Purchase purchase, List<Exception> exceptions)
        {
            if (purchase.CardHolder == null)
            {
                exceptions.Add(new ArgumentException("Card holder name can not be null"));
                return exceptions;
            }
            string[] cardHolderNameArray = purchase.CardHolder.ToString().Split(" ", StringSplitOptions.RemoveEmptyEntries | StringSplitOptions.TrimEntries);

            string cardHolderStringWhitespaceTrimmed = string.Join(" ", cardHolderNameArray);
            if (cardHolderStringWhitespaceTrimmed.Length < 2 | cardHolderStringWhitespaceTrimmed.Length > 22 | cardHolderNameArray.Length > 3 | cardHolderNameArray.Length < 2 | Regex.Match(cardHolderStringWhitespaceTrimmed, "[0-9]").Success)
                exceptions.Add(new ArgumentException("Enter a valid card holder name"));
            return exceptions;
        }

        /// <summary>
        /// Validates the CVV within the purchase object
        /// </summary>
        /// <param name="purchase">A purchase object to be persisted to the database</param>
        /// <param name="exceptions">A list of exceptions</param>
        /// <returns>Returns the list of exceptions with an exception added if there is an error</returns>
        public static List<Exception> ValidateCVV(Purchase purchase, List<Exception> exceptions)
        {
            if (!Regex.Match(purchase.CVV.ToString(), "^[0-9]{3}$").Success)
                exceptions.Add(new ArgumentException("Enter a valid CVV"));
            return exceptions;
        }

        /// <summary>
        /// Validates the credit card number within the purchase object
        /// </summary>
        /// <param name="purchase">A purchase object to be persisted to the database</param>
        /// <param name="exceptions">A list of exceptions</param>
        /// <returns>Returns the list of exceptions with an exception added if there is an error</returns>
        public static List<Exception> ValidateCardNumber(Purchase purchase, List<Exception> exceptions)
        {
            if (purchase.CardNumber == null)
            {
                exceptions.Add(new ArgumentException("Card number can not be null"));
                return exceptions;
            }

            if (!Regex.Match(purchase.CardNumber.ToString(), "^(51|52|53|54|55)[0-9]{14}$|^(4)[0-9]{15}$").Success)
                exceptions.Add(new ArgumentException("Enter a valid Visa or MasterCard card number"));
            return exceptions;

        }

        /// <summary>
        /// Validates the expiration date within the purchase object
        /// </summary>
        /// <param name="purchase">A purchase object to be persisted to the database</param>
        /// <param name="exceptions">A list of exceptions</param>
        /// <returns>Returns the list of exceptions with an exception added if there is an error</returns>
        public static List<Exception> ValidateExpiration(Purchase purchase, List<Exception> exceptions)
        {
            if (purchase.Expiration == null)
            {
                exceptions.Add(new ArgumentException("Expiration can not be null"));
                return exceptions;
            }

            if (!Regex.Match(purchase.Expiration.ToString(), "^0[1-9]|1[012]/[0-9]{2}$").Success)
                exceptions.Add((new ArgumentException("Enter a valid expiration date")));
            else
            {
                var dt = DateTime.Now;
                string[] expirationArray = purchase.Expiration.ToString().Split("/");
                int expMonth = Int32.Parse(expirationArray[0]);
                int expYear = Int32.Parse("20" + expirationArray[1]);
                int currentMonth = dt.Month;
                int currentYear = dt.Year;
                if (expYear < currentYear | (expYear == currentYear && expMonth < currentMonth))
                    exceptions.Add(new ArgumentException("Enter a valid expiration date"));
            }
            return exceptions;
        }
    }
}