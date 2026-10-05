using Catalyte.Apparel.Data.Models;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace Catalyte.Apparel.Providers.Interfaces
{
    /// <summary>
    /// This interface provides an abstraction layer for product related service methods.
    /// </summary>
    public interface IProductProvider
    {
        Task<IEnumerable<Product>> GetProductsAsync(List<string> category, List<string> demographic, List<string> brand, decimal? maxPrice, decimal? minPrice, List<string> color, List<string> material, bool? active);

        Task<Product> GetProductByIdAsync(int productId);

        Task<List<string>> GetProductCategoriesAsync();

        Task<List<string>> GetProductTypesAsync();

        Task<List<string>> GetProductBrandsAsync();

        Task<List<string>> GetProductDemographicsAsync();

        Task<List<string>> GetProductColorsAsync();

        Task<List<string>> GetProductMaterialsAsync();
        
        Task<Product> UpdateProductAsync(int id, Product productToUpdate);
        
        Task<Product> CreateProductAsync(Product newProduct);

    }
}
