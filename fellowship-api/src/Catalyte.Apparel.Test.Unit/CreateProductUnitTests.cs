/*using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Catalyte.Apparel.Data.Models;
using Xunit;
using Catalyte.Apparel.Providers;

namespace Catalyte.Apparel.Test.Unit
{
    public class CreateNewProductValidatorUnitTests
    {

        [Fact]
        public void Name_Field_Is_Empty_Throws_Correct_Exception_Message()
        {
            var exceptions = new List<Exception>();
            var post = new Product()
            {
                Name = "",
                Price = 1,
                Quantity = 1,
                Sku = "",
                Description = "",
                Demographic = "",
                Category = "",
                Type = "",
                Brand = "",
                Material = "",
                ImageSrc = "",
                PrimaryColorCode = "",
                SecondaryColorCode = "",
                StyleNumber = "",
                GlobalProductCode = "",
                ReleaseDate = DateTime.Now

            };
            Assert.Equal("Name is a required field", CreateNewProductValidator.ValidateName(post, exceptions)[0].Message);
        }


        [Fact]
        public void Name_Field_Alphanumeric_More_Than_Two_Characters_Does_Not_Throw_Exception()
        {
            var exceptions = new List<Exception>();
            var post = new Product()
            {
                Name = "",
                Price = 1,
                Quantity = 1,
                Sku = "",
                Description = "",
                Demographic = "",
                Category = "",
                Type = "",
                Brand = "",
                Material = "",
                ImageSrc = "",
                PrimaryColorCode = "",
                SecondaryColorCode = "",
                StyleNumber = "",
                GlobalProductCode = "",
                ReleaseDate = DateTime.Now
            };
            Assert.False(CreateNewProductValidator.ValidateName(post, exceptions).Any());
        }
    }
}
*/
