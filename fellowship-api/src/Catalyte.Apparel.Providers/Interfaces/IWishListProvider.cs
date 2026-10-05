using System.Threading.Tasks;
using Catalyte.Apparel.Data.Models;
namespace Catalyte.Apparel.Providers.Interfaces
{
    /// <summary>
    /// This interface provides an abstraction layer for WishList related service methods.
    /// </summary>
    public interface IWishListProvider
    {
        Task<WishListItem> CreateWishListItemAsync(WishListItem wishList, string userEmail);

        Task<WishListItem> GetWishListByEmailAsync(string userEmail);

        Task<WishListItem> GetWishListItemByProductIDAsync(int productID);

        Task DeleteWishListItemByProductIdAsync(int productID);

    }
}