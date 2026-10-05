using AutoMapper;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;
using Catalyte.Apparel.Providers.Interfaces;
using System.Threading.Tasks;
using Catalyte.Apparel.DTOs.WishListItems;
using Catalyte.Apparel.Data.Models;

namespace Catalyte.Apparel.API.Controllers
{
    /// <summary>
    /// The WishListController exposes endpoints for WishList related actions.
    /// </summary>

    [ApiController]
    [Route("/wishlist")]
    public class WishListItemController : ControllerBase
    {
        private readonly ILogger<WishListItemController> _logger;
        private readonly IWishListProvider _wishListProvider;
        private readonly IMapper _mapper;

        public WishListItemController(
            ILogger<WishListItemController> logger,
            IWishListProvider wishListProvider,
            IMapper mapper)
        {
            _logger = logger;
            _wishListProvider = wishListProvider;
            _mapper = mapper;
        }

        [HttpPost("{userEmail}")]
        public async Task<ActionResult<WishListItemDTO>> AddProductToWishListAsync([FromBody] WishListItem wishListItem, string userEmail)
        {
            _logger.LogInformation("Request received for AddProductToWishList");

            var newWishList = await _wishListProvider.CreateWishListItemAsync(wishListItem, userEmail);
            var wishListDTO = _mapper.Map<WishListItemDTO>(newWishList);

            return Created("/wishlist", wishListDTO);
        }

        [HttpDelete("{userEmail}/{productID}")]
        public async Task<ActionResult> DeleteWishListItemByProductIdAsync(string userEmail, int productID)
        {
            _logger.LogInformation($"Request received for DeleteWishListItemByProductIdAsync for email: {userEmail} and Product ID: {productID}");

            await _wishListProvider.DeleteWishListItemByProductIdAsync(productID);

            string removedItem = $"Item with Product ID of {productID}, has been removed from the wish list.";

            return Ok(removedItem);
        }

    }
}
