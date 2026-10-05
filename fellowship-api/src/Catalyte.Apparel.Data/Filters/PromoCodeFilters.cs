using Catalyte.Apparel.Data.Models;
using System.Collections.Generic;
using System.Linq;

namespace Catalyte.Apparel.Data.Filters
{
    /// <summary>
    /// Filter collection for promo code context queries.
    /// </summary>
    public static class PromoCodeFilters
    {
        public static IQueryable<PromoCode> WherePromoCodeTitleEquals(this IQueryable<PromoCode> promoCodes, string title)
        {
            return promoCodes.Where(p => p.title.ToUpper() == title.ToUpper()).AsQueryable();
        }

        /// <summary>
        /// Asynchronously retrieves promo codes from the database based on title.
        /// </summary>
        /// <param name="title">The category of the promo code to filter by.</param>
        /// <returns>All promo codes from database based on filters.</returns>
        public static IQueryable<PromoCode> WherePromoCodeFilterEquals(this IQueryable<PromoCode> promoCodes, List<int> id, List<string> title, List<string> description, List<string> type, List<decimal> rate)
        {
            if (title.Any())
                promoCodes = promoCodes.Where(p => title.Contains(p.title.ToUpper()));

            return promoCodes;
        }
    }
}
