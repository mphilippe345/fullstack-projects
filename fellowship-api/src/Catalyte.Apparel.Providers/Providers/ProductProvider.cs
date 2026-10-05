using Catalyte.Apparel.Data.Interfaces;
using Catalyte.Apparel.Data.Models;
using Catalyte.Apparel.Providers.Interfaces;
using Catalyte.Apparel.Utilities.HttpResponseExceptions;
using Microsoft.Extensions.Logging;
using System;
using System.Collections.Generic;
using System.Threading.Tasks;
using System.Text;
using Catalyte.Apparel.Providers.Validators;

namespace Catalyte.Apparel.Providers.Providers
{
    /// <summary>
    /// This class provides the implementation of the IProductProvider interface, providing service methods for products.
    /// </summary>
    public class ProductProvider : IProductProvider
    {
        private readonly ILogger<ProductProvider> _logger;
        private readonly IProductRepository _productRepository;
        public ProductProvider(IProductRepository productRepository, ILogger<ProductProvider> logger)
        {
            _logger = logger;
            _productRepository = productRepository;
        }

        /// <summary>
        /// Asynchronously retrieves products from the database based on filters.
        /// </summary>
        /// <param name="category">The category of the product to filter by.</param>
        /// <param name="demographic">The demographic of the product to filter by.</param>
        /// <param name="brand">The bran of the product to filter by.</param>
        /// <param name="maxPrice">The price of the product to filter by.</param>
        /// <param name="minPrice">The price of the product to filter by.</param>
        /// <param name="color">The color code of the product to filter by.</param>
        /// <param name="material">The material of the product to filter by.</param>
        /// <param name="active">The active status of the product to filter by.</param>
        /// <returns>All products from database based on filters.</returns>
        public async Task<IEnumerable<Product>> GetProductsAsync(List<string> category, List<string> demographic, List<string> brand, decimal? maxPrice, decimal? minPrice, List<string> color, List<string> material, bool? active)
        {
            IEnumerable<Product> products;

            if ((minPrice.HasValue && maxPrice.HasValue))
            {
                if ((minPrice > maxPrice) && (minPrice > 0 && maxPrice > 0))
                    throw new BadRequestException("Maximum price should be greater than the minimum price");

                if ((minPrice > maxPrice) && (minPrice < 0 && maxPrice < 0))
                    throw new BadRequestException("Maximum price should be greater than the minimum price. The minimum and the maximum prices cannot be negative numbers.");
            }

            if ((minPrice.HasValue && minPrice < 0) && (maxPrice.HasValue && maxPrice < 0))
                throw new BadRequestException("The minimum and the maximum prices cannot be negative numbers.");

            if ((minPrice.HasValue && minPrice < 0) && (maxPrice.HasValue && maxPrice < 0) && minPrice == maxPrice)
                throw new BadRequestException("The minimum and the maximum prices cannot be negative numbers.");

            if ((minPrice.HasValue && minPrice > 0) && (maxPrice.HasValue && maxPrice < 0))
                throw new BadRequestException("The maximum price cannot be a negative number and should be greater than the minimum price.");

            if (minPrice.HasValue && minPrice < 0)
                throw new BadRequestException("Minimum price cannot be a negative number");

            if (maxPrice.HasValue && maxPrice < 0)
                throw new BadRequestException("Maximum price cannot be a negative number");

            try
            {
                products = await _productRepository.GetProductsAsync(category, demographic, brand, maxPrice, minPrice, color, material, active);
            }
            catch (Exception ex)
            {
                _logger.LogError(ex.Message);
                throw new ServiceUnavailableException("There was a problem connecting to the database.");
            }

            return products;
        }

        /// <summary>
        /// Asynchronously retrieves the product with the provided id from the database.
        /// </summary>
        /// <param name="productId">The id of the product to retrieve.</param>
        /// <returns>The product.</returns>
        public async Task<Product> GetProductByIdAsync(int productId)
        {
            Product product;

            try
            {
                product = await _productRepository.GetProductByIdAsync(productId);
            }
            catch (Exception ex)
            {
                _logger.LogError(ex.Message);
                throw new ServiceUnavailableException("There was a problem connecting to the database.");
            }

            if (product == null || product == default)
            {
                _logger.LogInformation($"Product with id: {productId} could not be found.");
                throw new NotFoundException($"Product with id: {productId} could not be found.");
            }

            return product;
        }

        /// <summary>
        /// Asynchronously retrieves all unique product categories from the product database.
        /// </summary>
        /// <returns> All unique product categories in the product database </returns>
        /// <exception cref="ServiceUnavailableException"></exception>
        public async Task<List<string>> GetProductCategoriesAsync()
        {
            List<string> categoryList;

            try
            {
                categoryList = await _productRepository.GetProductCategoriesAsync();
            }
            catch (Exception ex)
            {
                _logger.LogError(ex.Message);
                throw new ServiceUnavailableException("There was a problem connecting to the database.");
            }
            return categoryList;
        }

        /// <summary>
        /// Asynchronously retrieves all unique product types from the product database.
        /// </summary>
        /// <returns> All unique product types in the product database </returns>
        /// <exception cref="ServiceUnavailableException"></exception>
        public async Task<List<string>> GetProductTypesAsync()
        {
            List<string> productTypes;

            try
            {
                productTypes = await _productRepository.GetProductTypesAsync();
            }
            catch (Exception ex)
            {
                _logger.LogError(ex.Message);
                throw new ServiceUnavailableException("There was a problem connecting to the database.");
            }

            return productTypes;
        }

        /// <summary>
        /// Persists a NewProduct to the database 
        /// </summary>
        /// <param name="newProduct">The newly created product to persist</param>
        /// <returns>The product</returns>
        public async Task<Product> CreateProductAsync(Product newProduct)
        {
            Product savedProduct;

            newProduct.DateCreated = DateTime.Now;
            newProduct.DateModified = DateTime.Now;

            try
            {
                savedProduct = await _productRepository.CreateProductAsync(newProduct);

            }
            catch (Exception ex)
            {
                _logger.LogError(ex.Message);
                throw new ServiceUnavailableException("There was a problem connecting to the database.");
            }

            return savedProduct;
        }

        /// <summary>
        /// Asynchronously updates the product after validating the product. 
        /// </summary>
        /// <param name="id">The id of the product to be updated.</param>
        /// <param name="productToUpdate">The product object to be updated.</param>
        /// <returns>The updated product.</returns>
        /// <exception cref="ServiceUnavailableException"></exception>
        /// <exception cref="NotFoundException"></exception>
        /// <exception cref="BadRequestException">If there are any errors</exception>
        public async Task<Product> UpdateProductAsync(int id, Product productToUpdate)
        {
            //Validate if product to update exists
            Product existingProduct;
            try
            {
                existingProduct = await _productRepository.GetProductByIdAsync(id);
            }
            catch (Exception ex)
            {
                _logger.LogError(ex.Message);
                throw new ServiceUnavailableException("There was a problem connecting to the database.");
            }

            if (existingProduct == null)
            {
                _logger.LogInformation($"Product with id: {id} does not exist.");
                throw new NotFoundException($"Product with id:{id} not found.");
            }

            //Logic to prevent properties from being updated
            productToUpdate.Id = existingProduct.Id;
            productToUpdate.DateCreated = existingProduct.DateCreated;

            //Timestamp the update
            productToUpdate.DateModified = DateTime.Now;

            StringBuilder sbErrors = new StringBuilder();
            sbErrors.Append("Incorrect product information: ");

            List<string> errors = new List<string>();
            errors = ProductValidator.ValidateProduct(productToUpdate);

            var countNulls = 0;
            for (int i = 0; i < errors.Count; i++)
            {
                if (errors[i] != null)
                {
                    var paranthesisForErrors = $" ({errors[i]}) ";
                    sbErrors.Append(paranthesisForErrors);
                }
                else if (errors[i] == null)
                {
                    countNulls++;
                }
                if (i == errors.Count - 1 && countNulls != errors.Count)
                {
                    throw new BadRequestException(sbErrors.ToString());
                }
            }

            try
            {
                await _productRepository.UpdateProductAsync(productToUpdate);
                _logger.LogInformation("Product Updated.");
            }
            catch (Exception ex)
            {
                _logger.LogError(ex.Message);
                throw new ServiceUnavailableException("There was a problem connecting to the database");
            }

            return productToUpdate;
        }

        /// <summary>
        /// Asynchronously retrieves all unique product brands from the product database.
        /// </summary>
        /// <returns> All unique product brands in the product database </returns>
        /// <exception cref="ServiceUnavailableException"></exception>
        public async Task<List<string>> GetProductBrandsAsync()
        {
            List<string> productBrands;

            try
            {
                productBrands = await _productRepository.GetProductBrandsAsync();
            }
            catch (Exception ex)
            {
                _logger.LogError(ex.Message);
                throw new ServiceUnavailableException("There was a problem connecting to the database.");
            }

            return productBrands;
        }

        /// <summary>
        /// Asynchronously retrieves all unique product demographics from the product database.
        /// </summary>
        /// <returns> All unique product demographics in the product database </returns>
        /// <exception cref="ServiceUnavailableException"></exception>
        public async Task<List<string>> GetProductDemographicsAsync()
        {
            List<string> productDemographics;

            try
            {
                productDemographics = await _productRepository.GetProductDemographicsAsync();
            }
            catch (Exception ex)
            {
                _logger.LogError(ex.Message);
                throw new ServiceUnavailableException("There was a problem connecting to the database.");
            }

            return productDemographics;
        }

        /// <summary>
        /// Asynchronously retrieves all unique product colors from the product database.
        /// </summary>
        /// <returns> All unique product colors in the product database </returns>
        /// <exception cref="ServiceUnavailableException"></exception>
        public async Task<List<string>> GetProductColorsAsync()
        {
            List<string> productColors;

            try
            {
                productColors = await _productRepository.GetProductColorsAsync();
            }
            catch (Exception ex)
            {
                _logger.LogError(ex.Message);
                throw new ServiceUnavailableException("There was a problem connecting to the database.");
            }

            return productColors;
        }

        /// <summary>
        /// Asynchronously retrieves all unique product materials from the product database.
        /// </summary>
        /// <returns> All unique product materials in the product database </returns>
        /// <exception cref="ServiceUnavailableException"></exception>
        public async Task<List<string>> GetProductMaterialsAsync()
        {
            List<string> productMaterials;

            try
            {
                productMaterials = await _productRepository.GetProductMaterialsAsync();
            }
            catch (Exception ex)
            {
                _logger.LogError(ex.Message);
                throw new ServiceUnavailableException("There was a problem connecting to the database.");
            }

            return productMaterials;

        }
    }
}

