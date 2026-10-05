/*using Catalyte.Apparel.Data.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;
namespace Catalyte.Apparel.Providers

{
    public class CreateNewProductValidator
    {
        /// <summary>
        /// Uses all product field validation helper methods
        /// </summary>
        /// <param name="createNewProduct">The current product trying to persist to the database</param>
        /// <returns>A list of exceptions for validation errors, if any</returns>
        /// 

        public static List<Exception> ValidateCreateNewProduct(Product post)
        {
            var exceptions = new List<Exception>();
//            ValidateActive(post, exceptions);
//            ValidateName(post, exceptions);
//            ValidatePrice(post, exceptions);
//            ValidateQuantity(post, exceptions);
//            ValidateSku(post, exceptions);
//            ValidateDescription(post, exceptions);
//            ValidateDemographic(post, exceptions);
//            ValidateCategory(post, exceptions);
//            ValidateType(post, exceptions);
//            ValidateBrand(post, exceptions);
//            ValidateMaterial(post, exceptions);
//            ValidateImageSrc(post, exceptions);
//            ValidatePrimaryColorCode(post, exceptions);
//            ValidateSecondaryColorCode(post, exceptions);
//            ValidateStyleNumber(post, exceptions);
//            ValidateGlobalProductCode(post, exceptions);
            return exceptions;
        }


        /// <summary>
        /// Validates the product name within the product object
        /// </summary>
        /// <param name="post">A post object to be persisted to the database</param>
        /// <param name="exceptions">A list of exceptions</param>
        /// <returns>Returns the list of exceptions with an exception added if there is an error</returns>
             public static List<Exception> ValidateActive(Product post, List<Exception> exceptions)
        {
            if (post.Name == null)
            {
                exceptions.Add(new ArgumentException("Name can not be null"));
                return exceptions;
            }
            string[] nameArray = Product.Name.ToString().Split(" ", StringSplitOptions.RemoveEmptyEntries | StringSplitOptions.TrimEntries);

            string cardHolderStringWhitespaceTrimmed = string.Join(" ", nameArray);
            if (cardHolderStringWhitespaceTrimmed.Length < 2 | cardHolderStringWhitespaceTrimmed.Length > 40 | nameArray.Length > 3 | nameArray.Length < 2 | Regex.Match(cardHolderStringWhitespaceTrimmed, "[0-9]").Success)
                exceptions.Add(new ArgumentException("Enter a valid card holder name"));
            else
            {
                if (nameArray.Length == 3)
                {
                    if (!(nameArray[1].Length == 1))
                        exceptions.Add(new ArgumentException("Enter a valid card holder name"));
                }
            }
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
        } */
