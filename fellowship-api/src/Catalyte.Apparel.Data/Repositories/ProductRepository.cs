using Catalyte.Apparel.Data.Context;
using Catalyte.Apparel.Data.Filters;
using Catalyte.Apparel.Data.Interfaces;
using Catalyte.Apparel.Data.Models;
using Microsoft.EntityFrameworkCore;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Catalyte.Apparel.Data.Repositories
{
    /// <summary>
    /// This class handles methods for making requests to the product repository.
    /// </summary>
    public class ProductRepository : IProductRepository
    {
        private readonly IApparelCtx _ctx;

        public ProductRepository(IApparelCtx ctx)
        {
            _ctx = ctx;
        }

        /// <summary>
        /// Asynchronously retrieves products from the database based on filters.
        /// </summary>
        /// <param name="category">The category of the product to filter by.</param>
        /// <param name="demographic">The demographic of the product to filter by.</param>
        /// <param name="brand">The brand of the product to filter by.</param>
        /// <param name="maxPrice">The max price of the product to filter by.</param>
        /// <param name="minPrice">The min price of the product to filter by.</param>
        /// <param name="color">The color code of the product to filter by.</param>
        /// <param name="material">The material of the product to filter by.</param>
        /// <param name="active">The active status of the product to filter by.</param>
        /// <returns>All products from database based on filters.</returns>
        public async Task<IEnumerable<Product>> GetProductsAsync(List<string> category, List<string> demographic, List<string> brand, decimal? maxPrice, decimal? minPrice, List<string> color, List<string> material, bool? active)
        {
            return await _ctx.Products
                .Include(p => p.Reviews)
                .AsNoTracking()
                .WhereProductFilter(category, demographic, brand, maxPrice, minPrice, color, material, active)
                .OrderBy(id => id)
                .ToListAsync();
        }

        public async Task<Product> GetProductByIdAsync(int productId)
        {
            return await _ctx.Products
                .Include(p => p.Reviews)
                .AsNoTracking()
                .WhereProductIdEquals(productId)
                .SingleOrDefaultAsync();
        }

        public async Task<List<string>> GetProductCategoriesAsync()
        {
            HashSet<string> categories = new();
            await _ctx.Products.ForEachAsync(product => categories.Add(product.Category));

            return categories.OrderBy(category => category).ToList();
        }

        public async Task<List<string>> GetProductTypesAsync()
        {
            HashSet<string> productTypes = new();
            await _ctx.Products.ForEachAsync(product => productTypes.Add(product.Type));

            return productTypes.OrderBy(type => type).ToList();
        }

        public async Task<List<string>> GetProductBrandsAsync()
        {
            HashSet<string> brands = new();
            await _ctx.Products.ForEachAsync(product => brands.Add(product.Brand));

            return brands.OrderBy(brands => brands).ToList();
        }

        public async Task<List<string>> GetProductDemographicsAsync()
        {
            HashSet<string> demographics = new();
            await _ctx.Products.ForEachAsync(product => demographics.Add(product.Demographic));

            return demographics.OrderBy(demographics => demographics).ToList();
        }

        public async Task<List<string>> GetProductColorsAsync()
        {
            HashSet<string> colors = new();
            await _ctx.Products.ForEachAsync(product => colors.Add(product.PrimaryColorCode));

            return colors.OrderBy(colors => colors).ToList();
        }

        public async Task<List<string>> GetProductMaterialsAsync()
        {
            HashSet<string> materials = new();
            await _ctx.Products.ForEachAsync(product => materials.Add(product.Material));

            return materials.OrderBy(materials => materials).ToList();
        }

        public async Task<Product> UpdateProductAsync(Product productToUpdate)
        {
            _ctx.Products.Update(productToUpdate);
            await _ctx.SaveChangesAsync();
            return productToUpdate;
        }

        public async Task<Product> CreateProductAsync(Product newProduct)
        {
            await _ctx.Products.AddAsync(newProduct);
            await _ctx.SaveChangesAsync();
            return newProduct;
        }
    }
}

