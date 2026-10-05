using Catalyte.Apparel.Data.Interfaces;
using Catalyte.Apparel.Data.Models;
using Catalyte.Apparel.Providers.Interfaces;
using Catalyte.Apparel.Providers.Providers;
using Catalyte.Apparel.Utilities.HttpResponseExceptions;
using Microsoft.Extensions.Logging;
using Moq;
using System;
using System.Collections.Generic;
using System.Threading.Tasks;
using Xunit;
using FluentAssertions;

namespace Catalyte.Apparel.Test.Unit
{
    public class ProductUnitTests
    {
        private readonly List<Product> products;
        private readonly IProductProvider productProvider;
        private readonly Mock<IProductRepository> productRepo;
        private readonly Mock<ILogger<ProductProvider>> logger;

        public ProductUnitTests()
        {
            productRepo = new Mock<IProductRepository>();
            logger = new Mock<ILogger<ProductProvider>>();
            productProvider = new ProductProvider(productRepo.Object, logger.Object);
            products = new List<Product>()
            {
                new Product()
                {
                    Id = 1,
                    DateCreated = DateTime.Now,
                    DateModified = DateTime.Now,
                    Name = "Sports shoes",
                    Sku = "ABC-NTUK-RED",
                    Description = "Shoes that are built for sport activities such as Soccer, or Football.",
                    Demographic = "Men",
                    Category = "Sport",
                    Type = "Shoe",
                    ReleaseDate = DateTime.Now,
                    PrimaryColorCode = "#ffffff",
                    SecondaryColorCode = "#ffffff",
                    StyleNumber = "scDHBJI",
                    GlobalProductCode = "po-XYCUQBH",
                    Active = true,
                },
                new Product()
                {
                    Id = 2,
                    DateCreated = DateTime.Now,
                    DateModified = DateTime.Now,
                    Name = "Steel-Toed Sneaker Boots",
                    Sku = "AYC-SRG-BlUE",
                    Description = "Sneaker styled boots designed for comfort, with metal steel at the toe for foot protection.",
                    Demographic = "Men",
                    Category = "Work",
                    Type = "Shoe",
                    ReleaseDate = DateTime.Now,
                    PrimaryColorCode = "#ffffff",
                    SecondaryColorCode = "#ffffff",
                    StyleNumber = "scDYJGJ",
                    GlobalProductCode = "po-SREOGIH",
                    Active = true,
                },

                new Product()
                {
                    Id = 3,
                    DateCreated = DateTime.Now,
                    DateModified = DateTime.Now,
                    Name = "Mountain Climbing Shoes",
                    Sku = "ABC-SDR-KJ",
                    Description = "Boots designed for mountain climbing, the bottom of the boots have terrain grip.",
                    Demographic = "Women",
                    Category = "Outdoors",
                    Type = "Shoe",
                    ReleaseDate = DateTime.Now,
                    PrimaryColorCode = "#ffffff",
                    SecondaryColorCode = "#ffffff",
                    StyleNumber = "scFOHBK",
                    GlobalProductCode = "po-WEOTJHJ",
                    Active = false,
                },

                new Product()
                {
                    Id = 4,
                    DateCreated = DateTime.Now,
                    DateModified = DateTime.Now,
                    Name = "Baseball Cap",
                    Sku = "ABC-EMF-SM",
                    Description = "Hat designed for maximum comfort during baseball games, has snapback functionaility.",
                    Demographic = "Children",
                    Category = "Sport",
                    Type = "Hat",
                    ReleaseDate = DateTime.Now,
                    PrimaryColorCode = "#ffffff",
                    SecondaryColorCode = "#39add1",
                    StyleNumber = "scDFKJB",
                    GlobalProductCode = "po-WREOGIH",
                    Active = true,
                },

                new Product()
                {
                    Id = 5,
                    DateCreated = DateTime.Now,
                    DateModified = DateTime.Now,
                    Name = "Comfortable Basketball Shorts",
                    Sku = "ABC-WJD-RD",
                    Description = "Shorts that are designed for comfort during basketball games.",
                    Demographic = "Men",
                    Category = "Sport",
                    Type = "Short",
                    ReleaseDate = DateTime.Now,
                    PrimaryColorCode = "#ffffff",
                    SecondaryColorCode = "#e15258",
                    StyleNumber = "scWEKFH",
                    GlobalProductCode = "po-RYURKDL",
                    Active = true,
                },

                new Product()
                {
                    Id = 6,
                    DateCreated = DateTime.Now,
                    DateModified = DateTime.Now,
                    Name = "UltraViolet Protection Sunglasses",
                    Sku = "ABC-BMF-LRG",
                    Description = "Sunglasses designed to protect eyes from harsh UV rays from the sun.",
                    Demographic = "Women",
                    Category = "Outdoors",
                    Type = "Sunglasses",
                    ReleaseDate = DateTime.Now,
                    PrimaryColorCode = "#ffffff",
                    SecondaryColorCode = "#ffffff",
                    StyleNumber = "scDJGKF",
                    GlobalProductCode = "po-EOWKLHL",
                    Active = true,
                }
            };
        }

        [Fact]
        public void GetProducts_RepoReturnsProducts_ReturnsAllProducts()
        {
            productRepo.Setup(m => m.GetProductsAsync(null, null, null, null, null, null, null, null)).ReturnsAsync(products);
            var expected = products;
            var actual = productProvider.GetProductsAsync(null, null, null, null, null, null, null, null).Result;
            Assert.Equal(expected, actual);
        }

        [Fact]
        public void GetProductsCategories_ReturnsAllProductCategories()
        {
            List<string> expected = new() { "Sport", "Work", "Outdoors" };
            productRepo.Setup(m => m.GetProductCategoriesAsync()).ReturnsAsync(expected);

            var actual = productProvider.GetProductCategoriesAsync().Result;
            Assert.Equal(expected, actual);
        }

        [Fact]
        public async Task GetProductsCategories_ReturnsServiceUnavailableErrorAsync()
        {
            string expected = "There was a problem connecting to the database.";
            productRepo.Setup(m => m.GetProductCategoriesAsync()).Throws(new ServiceUnavailableException(expected));

            Func<Task> result = async () => { await productProvider.GetProductCategoriesAsync(); };

            await result.Should().ThrowAsync<ServiceUnavailableException>();
        }

        [Fact]
        public void GetProductTypes_ReturnsAllUniqueTypes()
        {
            List<string> expected = new() { "Shoe", "Hat", "Short", "Sunglasses" };
            productRepo.Setup(m => m.GetProductTypesAsync()).ReturnsAsync(expected);

            var actual = productProvider.GetProductTypesAsync().Result;
            Assert.Equal(expected, actual);
        }

        [Fact]
        public async Task GetProductTypes_ReturnsServiceUnavailableErrorAsync()
        {
            string expected = "There was a problem connecting to the database.";
            productRepo.Setup(m => m.GetProductTypesAsync()).Throws(new ServiceUnavailableException(expected));

            Func<Task> result = async () => { await productProvider.GetProductTypesAsync(); };

            await result.Should().ThrowAsync<ServiceUnavailableException>();
        }
    }
}