using Catalyte.Apparel.Data.Models;
using System;
using System.Collections.Generic;
using System.Text;

namespace Catalyte.Apparel.Data.SeedData
{
    /// <summary>
    /// This class provides tools for generating random products.
    /// </summary>
    public class ProductFactory
    {
        Random _rand = new();

        private List<int> StyleNumbers = new() { };
        private List<int> poNumbers = new() { };

        private List<string> _colors = new()
        {
            "#000000", // white
            "#ffffff", // black
            "#39add1", // light blue
            "#3079ab", // dark blue
            "#c25975", // mauve
            "#e15258", // red
            "#f9845b", // orange
            "#838cc7", // lavender
            "#7d669e", // purple
            "#53bbb4", // aqua
            "#51b46d", // green
            "#e0ab18", // mustard
            "#637a91", // dark gray
            "#f092b0", // pink
            "#b7c0c7"  // light gray
        };

        private readonly List<string> _demographics = new()
        {
            "Men",
            "Women",
            "Kids"
        };

        private readonly List<string> _categories = new()
        {
            "Golf",
            "Soccer",
            "Basketball",
            "Hockey",
            "Football",
            "Running",
            "Baseball",
            "Skateboarding",
            "Boxing",
            "Weightlifting"
        };

        private List<string> _adjectives = new()
        {
            "Lightweight",
            "Slim",
            "Shock Absorbing",
            "Exotic",
            "Elastic",
            "Fashionable",
            "Trendy",
            "Next Gen",
            "Colorful",
            "Comfortable",
            "Water Resistant",
            "Wicking",
            "Heavy Duty"
        };

        private List<string> _types = new()
        {
            "Pant",
            "Short",
            "Shoe",
            "Glove",
            "Jacket",
            "Tank Top",
            "Sock",
            "Sunglasses",
            "Hat",
            "Helmet",
            "Belt",
            "Visor",
            "Shin Guard",
            "Elbow Pad",
            "Headband",
            "Wristband",
            "Hoodie",
            "Flip Flop",
            "Pool Noodle"
        };

        private List<String> _brand = new()
        {
            "Patagonia",
            "Merrell",
            "Altra",
            "Lowa",
            "Five Ten",
            "Teva",
            "Keen"
        };

        private List<String> _images = new()
        {
            "https://columbia.scene7.com/is/image/ColumbiaSportswear2/1962351_TERRABEE_ALT_9?$pra_pdp@2x$&v=1642418206",
            "https://columbia.scene7.com/is/image/ColumbiaSportswear2/1962831_MANZANITABEARSQUEEZE_ALT_9?$pra_pdp@2x$&v=1642418206",
            "https://cdn.shopify.com/s/files/1/1865/9377/products/com_is_image_ColumbiaSportswear2__28_ecbeb946-3341-4392-8996-b40ca6b00c7d_540x.jpg?v=1631738580",
            "https://cdn.shopify.com/s/files/1/1865/9377/products/com_is_image_ColumbiaSportswear2__29_f6f35b37-f50d-4746-a5c9-e145bfc51185_540x.jpg?v=1631738580",
            "https://cdn.shopify.com/s/files/1/1865/9377/products/U5JOUR315_CARGOTREEHUGGER_ALT_9_540x.jpg?v=1631738580",
            "https://cdn.shopify.com/s/files/1/1865/9377/products/U5JOUR315_EARTHBOUNDVAN_ALT_9_540x.jpg?v=1631738663"
        };

        private List<string> _skuMods = new()
        {
            "Blue",
            "Red",
            "KJ",
            "SM",
            "RD",
            "LRG",
            "SM"
        };

        private List<string> _materials = new()
        {
            "Cotton",
            "Wool",
            "Silk",
            "Leather",
            "Synthetic",
            "Velvet",
            "Satin",
            "Linen",
            "Denim",
            "Chiffon"
        };

        /// <summary>
        /// Generates a randomized product SKU.
        /// </summary>
        /// <returns>A SKU string.</returns>
        private string GetRandomSku()
        {
            var builder = new StringBuilder();
            builder.Append(RandomString(3));
            builder.Append('-');
            builder.Append(RandomString(3));
            builder.Append('-');
            builder.Append(_skuMods[_rand.Next(0, 6)]);

            return builder.ToString().ToUpper();
        }

        /// <summary>
        /// Returns a random demographic from the list of demographics.
        /// </summary>
        /// <returns>A demographic string.</returns>
        private string GetDemographic()
        {
            return _demographics[_rand.Next(0, 2)];
        }

        /// <summary>
        /// Generates a unique production code number
        /// </summary>
        /// <returns>A string with "po-" and a unique number</returns>
        private string GetRandomProductId()
        {
            return "po-" + RandomNumber(99_999, poNumbers);
        }

        /// <summary>
        /// Generates a random style code.
        /// </summary>
        /// <returns>A style code string.</returns>
        private string GetStyleCode(string demographic, int maxLength)
        {
            var builder = new StringBuilder();
            builder.Append(demographic[0]);
            builder.Append(RandomNumber(maxLength, StyleNumbers));
            return builder.ToString().ToUpper();
        }

        /// <summary>
        /// Generates a number of random products based on input.
        /// </summary>
        /// <param name="numberOfProducts">The number of random products to generate.</param>
        /// <returns>A list of random products.</returns>
        public List<Product> GenerateRandomProducts(int numberOfProducts)
        {

            var productList = new List<Product>();

            for (var i = 0; i < numberOfProducts; i++)
            {
                productList.Add(CreateRandomProduct(i + 1));
            }

            return productList;
        }

        /// <summary>
        /// Generates a random string from the list inputed
        /// </summary>
        /// <param name="list"></param>
        /// <returns>A random string for any list provided</returns>
        private string GetRandFromList(List<string> list)
        {
            return list[_rand.Next(list.Count)];
        }

        /// <summary>
        /// Generates a random boolean.
        /// </summary>
        /// <returns>A random boolean</returns>
        private Boolean GetRandomBoolean()
        {
            return _rand.NextDouble() < 0.5;
        }

        /// <summary>
        /// Generates the description of the product using the category, demographic, and an adjective that was generated
        /// </summary>
        /// <param name="category">String of the category of the product</param>
        /// <param name="demographic">String of the demographic of the product</param>
        /// <param name="adjective">String of the adjective that was generated</param>
        /// <returns></returns>
        private string GetDescription(string category, string demographic, string adjective)
        {
            var builder = new StringBuilder();
            builder.Append(category);
            builder.Append(" ");
            builder.Append(demographic);
            builder.Append(" ");
            builder.Append(adjective);
            return builder.ToString();
        }

        /// <summary>
        /// This generates a name for the product
        /// </summary>
        /// <param name="adj">String of the adjective</param>
        /// <param name="cat">String of the category</param>
        /// <param name="type">String of the type</param>
        /// <returns>A string of the name of the product</returns>
        private string GetName(string adj, string cat, string type)
        {
            var builder = new StringBuilder();
            builder.Append(adj);
            builder.Append(" ");
            builder.Append(cat);
            builder.Append(" ");
            builder.Append(type);
            return builder.ToString();
        }

        private DateTime GetRandomDate()
        {
            var now = DateTime.Now;
            var startDate = now.AddYears(-5);
            var endDate = now.AddYears(0);
            var range = Convert.ToInt32(endDate.Subtract(startDate).TotalDays);
            return startDate.AddDays(_rand.Next(range));
        }

        private decimal getPrice()
        {
            decimal price = decimal.Round(new decimal(_rand.NextDouble() * 300.00 + 0.00), 2, MidpointRounding.AwayFromZero);
            return price;
        }

        /// <summary>
        /// Uses random generators to build a products.
        /// </summary>
        /// <param name="id">ID to assign to the product.</param>
        /// <returns>A randomly generated product.</returns>
        private Product CreateRandomProduct(int id)
        {
            string demographic = GetRandFromList(_demographics);
            string type = GetRandFromList(_types);
            string category = GetRandFromList(_categories);
            string adjective = GetRandFromList(_adjectives);
            var date = GetRandomDate();

            return new Product
            {
                Id = id,
                Name = GetName(category, demographic, type),
                Brand = GetRandFromList(_brand),
                ImageSrc = GetRandFromList(_images),
                Quantity = _rand.Next(0, 100),
                Price = getPrice(),
                Category = category,
                Type = type,
                Material = GetRandFromList(_materials),
                Sku = GetRandomSku(),
                Demographic = demographic,
                Description = GetDescription(category, demographic, adjective),
                GlobalProductCode = GetRandomProductId(),


                StyleNumber = GetStyleCode(demographic, 9_999),
                PrimaryColorCode = GetRandFromList(_colors),
                SecondaryColorCode = GetRandFromList(_colors),
                ReleaseDate = date,
                DateCreated = date,
                DateModified = date,
                Active = GetRandomBoolean()
            };
        }

        /// <summary>
        /// Generates a random string of characters.
        /// </summary>
        /// <param name="size">Number of characters in the string.</param>
        /// <param name="lowerCase">Boolean if the character string should be lowercase only; defaults to false.</param>
        /// <returns>A random string of characters.</returns>
        private string RandomString(int size, bool lowerCase = false)
        {

            // ** Learning moment **
            // Code From
            // https://www.c-sharpcorner.com/article/generating-random-number-and-string-in-C-Sharp/

            // ** Learning moment **
            // Always use a string builder when concatenating more than a couple of strings.
            // Why? https://www.geeksforgeeks.org/c-sharp-string-vs-stringbuilder/
            var builder = new StringBuilder(size);

            // Unicode/ASCII Letters are divided into two blocks
            // (Letters 65–90 / 97–122):
            // The first group containing the uppercase letters and
            // the second group containing the lowercase.  

            // char is a single Unicode character  
            char offset = lowerCase ? 'a' : 'A';
            const int lettersOffset = 26; // A...Z or a..z: length=26  

            for (var i = 0; i < size; i++)
            {
                // ** Learning moment **
                // Because 'char' is a reserved word you can put '@' at the beginning to allow
                // its use as a variable name.  You could do the same thing with 'class'
                var @char = (char)_rand.Next(offset, offset + lettersOffset);
                builder.Append(@char);
            }

            return lowerCase ? builder.ToString().ToLower() : builder.ToString();
        }

        /// <summary>
        /// This generates a unique series of numbers while cross-referencing a list provided. If it
        /// matches with any number in the list, it will run the method again till it gets a number that
        /// is unique
        /// </summary>
        /// <param name="maxNum">The maximum number that the random generator will go up to</param>
        /// <param name="uniqueNumbersList">The list that is being cross-referenced to</param>
        /// <returns>A unique number</returns>
        private int RandomNumber(int maxNum, List<int> uniqueNumbersList)
        {
            int number = _rand.Next(0, maxNum);
            if (uniqueNumbersList.Contains(number))
            {
                RandomNumber(maxNum, uniqueNumbersList);
            }
            uniqueNumbersList.Add(number);
            return number;
        }
    }
}
