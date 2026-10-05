using Catalyte.Apparel.Data.Context;
using Catalyte.Apparel.Data.Interfaces;
using Catalyte.Apparel.Data.Models;
using System.Threading.Tasks;
using Microsoft.EntityFrameworkCore;
using Catalyte.Apparel.Data.Filters;


namespace Catalyte.Apparel.Data.Repositories
{
    /// <summary>
    /// This class handles methods for making requests to the WishListRepository.
    /// </summary>
    public class WishListRepository : IWishListRepository
    {
        private readonly IApparelCtx _ctx;

        public WishListRepository(IApparelCtx ctx)
        {
            _ctx = ctx;
        }

        /// <summary>
        /// Creates a wishlist item
        /// </summary>
        /// <param name="wishlist">Wish list item to create</param>
        /// <returns></returns>
        public async Task<WishListItem> CreateWishListItemAsync(WishListItem wishlist)
        {
            _ctx.WishList.Add(wishlist);
            await _ctx.SaveChangesAsync();
            return wishlist;
        }

        public async Task DeleteWishListItemByProductIdAsync(WishListItem wishListItem)
        {
            _ctx.WishList.Remove(wishListItem);
            await _ctx.SaveChangesAsync();
        }

        public async Task<WishListItem> GetWishListByEmailAsync(string userEmail)
        {
            return await _ctx.WishList
                .AsNoTracking()
                .WhereWishListEmailEquals(userEmail)
                .SingleOrDefaultAsync();
        }

        public async Task<WishListItem> GetWishListItemByProductIDAsync(int productID)
        {
            return await _ctx.WishList
                .AsNoTracking()
                .WhereWishListProductIDEquals(productID)
                .SingleOrDefaultAsync();
        }
    }
}