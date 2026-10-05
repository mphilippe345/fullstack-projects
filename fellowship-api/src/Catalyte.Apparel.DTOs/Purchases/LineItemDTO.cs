using Catalyte.Apparel.DTOs.Products;

namespace Catalyte.Apparel.DTOs.Purchases
{
    /// <summary>
    /// Describes a data transfer objecct for a single line item of a purchase transaction.
    /// </summary>
    public class LineItemDTO
    {
        public int ProductId { get; set; }

        public int Quantity { get; set; }

        public ProductDTO Product { get; set; }

    }
}
