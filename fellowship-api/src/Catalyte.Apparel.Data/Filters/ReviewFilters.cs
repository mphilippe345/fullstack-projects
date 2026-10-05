using Catalyte.Apparel.Data.Models;
using System.Linq;

namespace Catalyte.Apparel.Data.Filters
{
    /// <summary>
    /// Filter collection for review context queries.
    /// </summary>
    public static class ReviewFilters
    {
        public static IQueryable<Review> WhereIdEquals(this IQueryable<Review> reviews, int id)
        {
            return reviews.Where(r => r.Id == id).AsQueryable();
        }
    }
}
