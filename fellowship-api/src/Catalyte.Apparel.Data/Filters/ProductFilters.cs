using Catalyte.Apparel.Data.Models;
using System.Collections.Generic;
using System.Linq;

namespace Catalyte.Apparel.Data.Filters
{
    /// <summary>
    /// Filter collection for product context queries.
    /// </summary>
    public static class ProductFilters
    {
        public static IQueryable<Product> WhereProductIdEquals(this IQueryable<Product> products, int productId)
        {
            return products.Where(p => p.Id == productId).AsQueryable();
        }

        /// <summary>
        /// Asynchronously retrieves products from the database based on filters.
        /// </summary>
        /// <param name="categories">The category of the product to filter by.</param>
        /// <param name="demographics">The demographic of the product to filter by.</param>
        /// <param name="brands">The bran of the product to filter by.</param>
        /// <param name="maxPrice">The price of the product to filter by.</param>
        /// <param name="minPrice">The price of the product to filter by.</param>
        /// <param name="colors">The color code of the product to filter by.</param>
        /// <param name="materials">The material of the product to filter by.</param>
        /// <param name="active">The active status of the product to filter by.</param>
        /// <returns>All products from database based on filters.</returns>
        public static IQueryable<Product> WhereProductFilter(this IQueryable<Product> products, List<string> categories, List<string> demographics, List<string> brands, decimal? maxPrice, decimal? minPrice, List<string> colors, List<string> materials, bool? active)
        {
            if (brands.Any())
                products = products.Where(p => brands.Contains(p.Brand));

            if (categories.Any())
                products = products.Where(p => categories.Contains(p.Category));

            if (demographics.Any())                                            
                products = products.Where(p => demographics.Contains(p.Demographic));

            if (maxPrice.HasValue)
                products = products.Where(p => p.Price <= maxPrice);

            if (minPrice.HasValue)
                products = products.Where(p => p.Price >= minPrice);

            if (colors.Any())
                products = products.Where(p => colors.Contains(p.PrimaryColorCode) || colors.Contains(p.SecondaryColorCode));

            if (materials.Any())
                products = products.Where(p => materials.Contains(p.Material));

            if (active != null)
                products = products.Where(p => p.Active == active);

            return products;
        }
    }
}
