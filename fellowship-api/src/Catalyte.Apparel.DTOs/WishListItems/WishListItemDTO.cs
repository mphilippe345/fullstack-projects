using Catalyte.Apparel.DTOs.Products;

namespace Catalyte.Apparel.DTOs.WishListItems
{
    /// <summary>
    /// Describes a data transfer object for a WishListItem.
    /// </summary>

    public class WishListItemDTO
    {
        public string UserEmail { get; set; }

        public int UserID { get; set; }

        public int ProductID { get; set; }

        public string ProductName { get; set; }

    }
}