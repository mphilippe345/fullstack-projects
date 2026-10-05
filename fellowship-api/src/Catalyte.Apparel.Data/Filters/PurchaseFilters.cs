using Catalyte.Apparel.Data.Models;
using System.Linq;

namespace Catalyte.Apparel.Data.Filters
{
    /// <summary>
    /// Filter collection for purchase context queries.
    /// </summary>
    public static class PurchaseFilters
    {
        public static IQueryable<Purchase> WherePurchaseEmailEquals(this IQueryable<Purchase> purchases, string email)
        {
            return purchases.Where(p => p.BillingEmail == email);
        }
    }
}