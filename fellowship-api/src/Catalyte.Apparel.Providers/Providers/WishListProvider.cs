using System;
using System.Threading.Tasks;
using Catalyte.Apparel.Data.Interfaces;
using Catalyte.Apparel.Data.Models;
using Catalyte.Apparel.Providers.Interfaces;
using Catalyte.Apparel.Utilities.HttpResponseExceptions;
using Microsoft.Extensions.Logging;

namespace Catalyte.Apparel.Providers.Providers
{
    /// <summary>
    /// This class provides the implementation of the IWishListProvider interface, providing service methods for wish list tiems.
    /// </summary>
    public class WishListProvider : IWishListProvider
    {
        private readonly ILogger<IWishListProvider> _logger;
        private readonly IWishListRepository _wishListRepository;
        private readonly IUserProvider _userProvider;
        private readonly IProductProvider _productProvider;

        public WishListProvider(IWishListRepository wishListRepository, ILogger<WishListProvider> logger, IUserProvider userProvider, IProductProvider productProvider)
        {
            _logger = logger;
            _wishListRepository = wishListRepository;
            _userProvider = userProvider;
            _productProvider = productProvider;
        }

        /// <summary>
        /// Persists a wish list items to a user
        /// </summary>
        /// <param name="wishListItem">The wish list item to persist.</param>
        /// <returns>The review</returns>
        public async Task<WishListItem> CreateWishListItemAsync(WishListItem wishListItem, string userEmail)
        {
            wishListItem.DateCreated = DateTime.Now;
            wishListItem.DateModified = DateTime.Now;

            User user = await _userProvider.GetUserByEmailAsync(userEmail);
            Product product = await _productProvider.GetProductByIdAsync(wishListItem.ProductID);

            if (user == null)
            {
                throw new NotFoundException("User could not be found.");
            }

            foreach (WishListItem item in user.WishList)
            {
                if (item.ProductID == wishListItem.ProductID)
                {
                    throw new ConflictException("Product has already been added to wishlist.");
                }
            };

            wishListItem.ProductName = product.Name;
            wishListItem.UserID = user.Id;
            wishListItem.UserEmail = user.Email;

            WishListItem savedWishListItem;

            try
            {
                savedWishListItem = await _wishListRepository.CreateWishListItemAsync(wishListItem);

            }
            catch (Exception ex)
            {
                _logger.LogError(ex.Message);
                throw new ServiceUnavailableException("There was a problem connecting to the database.");
            }

            savedWishListItem.Product = product;

            return savedWishListItem;

        }

        public async Task DeleteWishListItemByProductIdAsync(int productID)
        {
            WishListItem wishListItem;

            wishListItem = await GetWishListItemByProductIDAsync(productID);

            try
            {
                await _wishListRepository.DeleteWishListItemByProductIdAsync(wishListItem);
            }
            catch (Exception ex)
            {
                _logger.LogError(ex.Message);
                throw new ServiceUnavailableException("There was a problem connecting to the database.");
            }

        }
        public async Task<WishListItem> GetWishListByEmailAsync(string userEmail)
        {
            WishListItem wishListItem;

            try
            {
                wishListItem = await _wishListRepository.GetWishListByEmailAsync(userEmail);
            }
            catch (Exception ex)
            {
                _logger.LogError(ex.Message);
                throw new ServiceUnavailableException("There was a problem connecting to the database.");
            }

            if (wishListItem == default)
            {
                _logger.LogError($"Could not find user wish list with email: {userEmail}");
                throw new NotFoundException($"Could not find user with email: {userEmail}");
            }

            return wishListItem;
        }

        public async Task<WishListItem> GetWishListItemByProductIDAsync(int productID)
        {
            WishListItem wishListItem;

            try
            {
                wishListItem = await _wishListRepository.GetWishListItemByProductIDAsync(productID);
            }
            catch (Exception ex)
            {
                _logger.LogError(ex.Message);
                throw new ServiceUnavailableException("There was a problem connecting to the database.");
            }

            if (wishListItem == default)
            {
                _logger.LogError($"Could not find item in wish list with id: {productID}");
                throw new NotFoundException($"Could not find item in wish list with id: {productID}");
            }

            return wishListItem;
        }

    }
}