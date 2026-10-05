using AutoMapper;
using System.Collections.Generic;
using System.Threading.Tasks;
using Catalyte.Apparel.DTOs.Products;
using Catalyte.Apparel.Providers.Interfaces;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;
using Catalyte.Apparel.Data.Models;

namespace Catalyte.Apparel.API.Controllers
{
    /// <summary>
    /// The ProductsController exposes endpoints for product related actions.
    /// </summary>
    [ApiController]
    [Route("/products")]
    public class ProductController : ControllerBase
    {
        private readonly ILogger<ProductController> _logger;
        private readonly IProductProvider _productProvider;
        private readonly IMapper _mapper;

        public ProductController(
            ILogger<ProductController> logger,
            IProductProvider productProvider,
            IMapper mapper)
        {
            _logger = logger;
            _mapper = mapper;
            _productProvider = productProvider;
        }

        [HttpGet]
        public async Task<ActionResult<IEnumerable<ProductDTO>>> GetProductsAsync(
            [FromQuery(Name = "brand")] List<string> brand,
            [FromQuery(Name = "category")] List<string> category,
            [FromQuery(Name = "color")] List<string> color,
            [FromQuery(Name = "demographic")] List<string> demographic,
            [FromQuery(Name = "material")] List<string> material,
            [FromQuery(Name = "maxPrice")] decimal? maxPrice,
            [FromQuery(Name = "minPrice")] decimal? minPrice,
            [FromQuery(Name = "active")] bool? active
           )

        {
            _logger.LogInformation("Request received for GetProductsAsync ");

            var products = await _productProvider.GetProductsAsync(category, demographic, brand, maxPrice, minPrice, color, material, active);
            var productDTOs = _mapper.Map<IEnumerable<ProductDTO>>(products);


            return Ok(productDTOs);
        }

        [HttpGet("{id}")]
        public async Task<ActionResult<ProductDTO>> GetProductByIdAsync(int id)
        {
            _logger.LogInformation($"Request received for GetProductByIdAsync for id: {id}");

            var product = await _productProvider.GetProductByIdAsync(id);
            var productDTO = _mapper.Map<ProductDTO>(product);

            return Ok(productDTO);
        }

        [HttpGet("categories")]
        public async Task<ActionResult<List<string>>> GetProductCategoriesAsync()
        {
            _logger.LogInformation($"Request received for GetProductCategoriesAsync");

            List<string> categories = await _productProvider.GetProductCategoriesAsync();

            return Ok(categories);
        }

        [HttpGet("types")]
        public async Task<ActionResult<List<string>>> GetProductTypesAsync()
        {
            _logger.LogInformation($"Request received for GetProductTypesAsync");

            List<string> productTypes = await _productProvider.GetProductTypesAsync();

            return Ok(productTypes);
        }

        [HttpGet("brands")]
        public async Task<ActionResult<List<string>>> GetProductBrandsAsync()
        {
            _logger.LogInformation($"Request received for GetProductBrandsAsync");

            List<string> productBrands = await _productProvider.GetProductBrandsAsync();

            return Ok(productBrands);
        }

        [HttpGet("demographics")]
        public async Task<ActionResult<List<string>>> GetProductDemographicsAsync()
        {
            _logger.LogInformation($"Request received for GetProductDemographicsAsync");

            List<string> productDemographics = await _productProvider.GetProductDemographicsAsync();

            return Ok(productDemographics);
        }
        [HttpGet("colors")]
        public async Task<ActionResult<List<string>>> GetProductColorsAsync()
        {
            _logger.LogInformation($"Request received for GetProductColorsAsync");

            List<string> productColors = await _productProvider.GetProductColorsAsync();

            return Ok(productColors);
        }

        [HttpGet("materials")]
        public async Task<ActionResult<List<string>>> GetProductMaterialsAsync()
        {
            _logger.LogInformation($"Request received for GetProductMaterialsAsync");

            List<string> productMaterials = await _productProvider.GetProductMaterialsAsync();

            return Ok(productMaterials);
        }

        [HttpPut("{id}")]
        public async Task<ActionResult<ProductDTO>> UpdateProductAsync(int id, [FromBody] Product productToUpdate)
        {
            _logger.LogInformation("Request recieved for UpdateProductAsync");

            var product = _mapper.Map<Product>(productToUpdate);
            var updatedProduct = await _productProvider.UpdateProductAsync(id, product);
            var ProductDTO = _mapper.Map<ProductDTO>(updatedProduct);

            return Ok(ProductDTO);
        }

        [HttpPost]
        public async Task<ActionResult<ProductDTO>> CreateProductAsync([FromBody] Product newProduct)
        {
            _logger.LogInformation("Request received for CreateProductAsync");

            var product = await _productProvider.CreateProductAsync(newProduct);
            var productDTO = _mapper.Map<ProductDTO>(product);

            return Created("/products", productDTO);
        }

    }
}

