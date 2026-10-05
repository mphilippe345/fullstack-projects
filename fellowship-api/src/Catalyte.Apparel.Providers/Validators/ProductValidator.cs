using System;
using System.Collections.Generic;
using Catalyte.Apparel.Data.Models;
using System.Text.RegularExpressions;

namespace Catalyte.Apparel.Providers.Validators
{
    internal class ProductValidator
    {
        /// <summary>
        /// Takes in the product and validates every field except ID, dateCreated and dateModified. If there are
        /// errors, it will return a list of strings to put thrown as an error.
        /// </summary>
        /// <param name="product">A product object that is being checked</param>
        /// <returns>A list of strings of error(s) if any</returns>
        public static List<String> ValidateProduct(Product product)
        {
            var errors = new List<String>();
            errors.Add(IsBoolean(product));
            errors.Add(ValidName(product));
            errors.Add(ValidPrice(product));
            errors.Add(ValidQuantiy(product));
            errors.Add(ValidSku(product));
            errors.Add(ValidDescription(product));
            errors.Add(ValidDemographic(product));
            errors.Add(ValidCategory(product));
            errors.Add(ValidType(product));
            errors.Add(ValidBrand(product));
            errors.Add(ValidMaterial(product));
            errors.Add(ValidImageSrc(product));
            errors.Add(ValidPrimaryColor(product));
            errors.Add(ValidSecondaryColor(product));
            errors.Add(ValidStyleNumber(product));
            errors.Add(ValidGlobalProductCode(product));
            errors.Add(ValidReleaseDate(product));
            return errors;
        }

        /// <summary>
        /// Takes in the product to see if the product Active field is true or false.
        /// </summary>
        /// <param name="product">A product object being validated</param>
        /// <returns>A string if there is an error or null.</returns>
        public static string IsBoolean(Product product)
        {
            if (product.Active != true && product.Active != false)
            {
                return "Product active field must be true or false.";
            }
            return null;
        }

        /// <summary>
        /// Takes in the product to validate the product name.
        /// </summary>
        /// <param name="product">>A product object being validated</param>
        /// <returns>A string if there is an error or null.</returns>
        public static string ValidName(Product product)
        {
            if (product.Name.Trim() == "")
            {
                return "Product name is required.";
            }
            else if (product.Name.Trim().Length < 3)
            {
                return "Product name must be more than 2 characters long.";
            }
            else if (product.Name.Trim().Length > 100)
            {
                return "Product name has a max character limit of 100.";
            }
            return null;
        }

        /// <summary>
        /// Takes in the product to validate the price.
        /// </summary>
        /// <param name="product">>A product object being validated</param>
        /// <returns>A string if there are any errors and null if none.</returns>
        public static string ValidPrice(Product product)
        {
            string validTwoDecimalPlaces = "^([1-9][0-9]{1,6}||[0-9])(\\.[0-9]{1,2})?$";
            Regex twoDecimalRegex = new Regex(validTwoDecimalPlaces);
            Match twoDecimalMatch = twoDecimalRegex.Match(product.Price.ToString());

            if (product.Price.ToString().Trim() == "")
            {
                return "Product price is required.";
            }
            else if (product.Price < 0)
            {
                return "Price cannot be negative.";
            }
            else if (product.Price >= 1_000_000)
            {
                return "Price must be less than $1,000,000.";
            }
            else if (!twoDecimalMatch.Success)
            {
                return "Price can have no more than 2 decimal places.";
            }
            return null;
        }

        /// <summary>
        /// Takes in a product object to validate the quantity field.
        /// </summary>
        /// <param name="product">>A product object being validated</param>
        /// <returns>A string if the is an error and null if none.</returns>
        public static string ValidQuantiy(Product product)
        {
            var validWholeNumber = "^[0-9]+$";
            if (product.Quantity.ToString().Trim() == "")
            {
                return "Product quantity is required.";
            }
            else if (product.Quantity < 0)
            {
                return "Product quantity cannot be less than 0";
            }
            else if (product.Quantity >= 1_000_000)
            {
                return "Product quantity must be less than 1,000,000";
            }
            else if (!Regex.Match(product.Quantity.ToString(), validWholeNumber).Success)
            {
                return "Product quantity only accepts whole numbers.";
            }
            return null;
        }

        /// <summary>
        /// Takes in a product object to validate the SKU field.
        /// </summary>
        /// <param name="product">>A product object being validated</param>
        /// <returns>A string if there are any errors and null if none.</returns>
        public static string ValidSku(Product product)
        {
            var validSku = "^([A-Za-z]{3}[-][A-Za-z]{3}[-][a-zA-Z]{2,4})$";
            if (product.Sku == null || product.Sku.Trim() == "")
            {
                return "Product sku is required.";
            }
            else if (!Regex.Match(product.Sku, validSku).Success)
            {
                return "Product SKU can accept alphabetic characters and hyphens only, e.g. \"AAA-BBB-CCC.\"";
            }
            return null;
        }

        /// <summary>
        /// Takes in a product object to validate the description.
        /// </summary>
        /// <param name="product">>A product object being validated</param>
        /// <returns>A string if there are any errors and null if none.</returns>
        public static string ValidDescription(Product product)
        {
            if (product.Description == null || product.Description.Trim() == "")
            {
                return "Product description is required.";
            }
            else if (product.Description.Length > 100)
            {
                return "Product description has a character limit of 100.";
            }
            return null;
        }

        /// <summary>
        /// Takes in a product object to validate the demographic.
        /// </summary>
        /// <param name="product">>A product object being validated</param>
        /// <returns>A string if there any errors and null if none.</returns>
        public static string ValidDemographic(Product product)
        {
            if (product.Demographic == null || product.Demographic.Trim() == "")
            {
                return "Product demographic is required.";
            }
            else if (product.Demographic != "Men" && product.Demographic != "Women" && product.Demographic != "Kids")
            {
                return "Product demographic must be only the following: Men, Women, or Kids.";
            }
            return null;
        }

        /// <summary>
        /// Takes in a product object to validate the category.
        /// </summary>
        /// <param name="product">>A product object being validated</param>
        /// <returns>A string if there is an error and null if none.</returns>
        public static string ValidCategory(Product product)
        {
            var alphanumericWhiteSpace = "^[a-zA-Z0-9\\s]*$";
            if (product.Category == null || product.Category.Trim() == "")
            {
                return "Product category is required.";
            }
            if (!Regex.Match(product.Category, alphanumericWhiteSpace).Success)
            {
                return "Product category cannot contain special characters.";
            }
            else if (product.Category.Length > 100)
            {
                return "Product category has a character limit of 100.";
            }
            return null;
        }

        /// <summary>
        /// Takes in a product object to validate the type.
        /// </summary>
        /// <param name="product">>A product object being validated</param>
        /// <returns>A string if there is an error and null if none.</returns>
        public static string ValidType(Product product)
        {
            var alphanumericWhiteSpace = "^[a-zA-Z0-9\\s]*$";
            if (product.Type == null || product.Type.Trim() == "")
            {
                return "Product type is required.";
            }
            else if (product.Type.Length > 100)
            {
                return "Product type has a character limit of 100.";
            }
            else if (!Regex.Match(product.Type, alphanumericWhiteSpace).Success)
            {
                return "Product type cannot contain special characters.";
            }
            return null;
        }

        /// <summary>
        /// Takes in a product object to validate the brand.
        /// </summary>
        /// <param name="product">>A product object being validated</param>
        /// <returns>A string if there are any errors and null if none.</returns>
        public static string ValidBrand(Product product)
        {
            if (product.Brand == null || product.Brand.Trim() == "")
            {
                return "Product brand is required.";
            }
            else if (product.Brand.Length > 100)
            {
                return "Product brand has a character limit of 100.";
            }
            return null;
        }

        /// <summary>
        /// Takes in a product object to validate the material.
        /// </summary>
        /// <param name="product">>A product object being validated</param>
        /// <returns>A string if there are any errors and null if none.</returns>
        public static string ValidMaterial(Product product)
        {
            var alphabeticWithWhitespace = "^[a-zA-Z]{0}[a-zA-Z\\s]+$";
            if (product.Material == null || product.Material.Trim() == "")
            {
                return "Product material is required.";
            }
            else if (product.Material.Length > 100)
            {
                return "Product material has a character limit of 100.";
            }
            else if (!Regex.Match(product.Material, alphabeticWithWhitespace).Success)
            {
                return "Product material must be only alphabetic.";
            }
            return null;
        }

        /// <summary>
        /// Takes in a product object to validate the image src.
        /// </summary>
        /// <param name="product">>A product object being validated</param>
        /// <returns>A string if there are any errors and null if none.</returns>
        public static string ValidImageSrc(Product product)
        {
            var validUrl = "(^(http:\\/\\/www\\.|https:\\/\\/www\\.|http:\\/\\/|https:\\/\\/)?[a-zA-Z0-9]+([\\-\\.]{1}[a-zA-Z0-9]+)*\\.[a-zA-Z]{2,5}(:[0-9]{1,5})?(\\/.*)?$)";

            if (product.ImageSrc == null || product.ImageSrc.Trim() == "")
            {
                return "Product image src is required.";
            }
            else if (!Regex.Match(product.ImageSrc, validUrl).Success)
            {
                return "Product image src must have a valid URL.";
            }
            else if (product.ImageSrc.Length > 150)
            {
                return "Product image src has a character limit of 150.";
            }
            return null;
        }

        /// <summary>
        /// Takes in a product object to validate the primary color.
        /// </summary>
        /// <param name="product">>A product object being validated.</param>
        /// <returns>A string if there are any errors and null if none.</returns>
        public static string ValidPrimaryColor(Product product)
        {
            var validHexCode = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$";
            if (product.PrimaryColorCode == null || product.PrimaryColorCode.Trim() == "")
            {
                return "Product primary color is required.";
            }
            else if (!Regex.Match(product.PrimaryColorCode, validHexCode).Success)
            {
                return "Product primary color must be a valid HEX code.";
            }
            return null;
        }

        /// <summary>
        /// Takes in a product object to validate the secondary color.
        /// </summary>
        /// <param name="product">A product object being validated.</param>
        /// <returns>A string if there any errors and null if none.</returns>
        public static string ValidSecondaryColor(Product product)
        {
            var validHexCode = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$";
            if (product.SecondaryColorCode == null || product.SecondaryColorCode.Trim() == "")
            {
                return "Product primary color is required.";
            }
            else if (!Regex.Match(product.SecondaryColorCode, validHexCode).Success)
            {
                return "Product secondary color must be a valid HEX code.";
            }
            return null;
        }

        /// <summary>
        /// Takes in a product object to validate the style number.
        /// </summary>
        /// <param name="product">A product object being validated.</param>
        /// <returns>A string if there are any errors and null if none.</returns>
        public static string ValidStyleNumber(Product product)
        {
            var validStyleNumber = "^([A-Z]{1}[0-9]{1,5})$";
            if (product.StyleNumber == null || product.StyleNumber.Trim() == "")
            {
                return "Product style number is required.";
            }
            else if (!Regex.Match(product.StyleNumber, validStyleNumber).Success || product.Demographic[0] != product.StyleNumber[0])
            {
                return "Product style number must begin with the first letter captitalized of the demographic, followed by 1-5 numbers.";
            }
            return null;
        }

        /// <summary>
        /// Takes in a product object to validate the global product code.
        /// </summary>
        /// <param name="product">A product object being validated.</param>
        /// <returns>A string if there any errors and null if none.</returns>
        public static string ValidGlobalProductCode(Product product)
        {
            var validGlobalProductCode = "^po-[0-9]{1,5}$";
            if (product.GlobalProductCode == null || product.GlobalProductCode.Trim() == "")
            {
                return "Product global product code is required.";
            }
            else if (!Regex.Match(product.GlobalProductCode, validGlobalProductCode).Success)
            {
                return "Product global product code must begin with \"po-\", followed by 1 to 5 numbers.";
            }
            return null;
        }

        /// <summary>
        /// Takes in the product object to validate the release date.
        /// </summary>
        /// <param name="product">A product object being validated.</param>
        /// <returns>A string if there are any errors and null if none.</returns>
        public static string ValidReleaseDate(Product product)
        {
            if (product.ReleaseDate.ToString().Trim() == "")
            {
                return "Product release date is required.";
            }
            return null;
        }
    }
}
