using Catalyte.Apparel.Data.Models;
using System.Linq;

namespace Catalyte.Apparel.Data.Filters
{
	/// <summary>
	/// Filter collection for user context queries.
	/// </summary>
	public static class WishListFilters
	{
		public static IQueryable<WishListItem> WhereWishListEmailEquals(this IQueryable<WishListItem> wishListItems, string UserEmail)
		{
			return wishListItems.Where(r => r.UserEmail == UserEmail).AsQueryable();
		}
		public static IQueryable<WishListItem> WhereWishListProductIDEquals(this IQueryable<WishListItem> wishListItems, int ProductID)
		{
			return wishListItems.Where(r => r.ProductID == ProductID).AsQueryable();
		}
	}
}
