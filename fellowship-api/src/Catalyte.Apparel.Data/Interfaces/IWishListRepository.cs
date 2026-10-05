using Catalyte.Apparel.Data.Models;
using System.Threading.Tasks;

namespace Catalyte.Apparel.Data.Interfaces
{
    /// <summary>
    /// This interface provides an abstraction layer for WishListRepository methods.
    /// </summary>

    public interface IWishListRepository
    {
        Task<WishListItem> CreateWishListItemAsync(WishListItem wishlist);

        Task DeleteWishListItemByProductIdAsync(WishListItem wishlist);

        Task<WishListItem> GetWishListByEmailAsync(string userEmail);

        Task<WishListItem> GetWishListItemByProductIDAsync(int productID);
    }
}