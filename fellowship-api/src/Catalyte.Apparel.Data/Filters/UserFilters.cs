using Catalyte.Apparel.Data.Models;
using System.Linq;

namespace Catalyte.Apparel.Data.Filters
{
    /// <summary>
    /// Filter collection for user context queries.
    /// </summary>
    public static class UserFilters
    {
        public static IQueryable<User> WhereUserEmailEquals(this IQueryable<User> users, string email)
        {
            return users.Where(u => u.Email == email).AsQueryable();
        }

        public static IQueryable<User> WhereUserIdEquals(this IQueryable<User> users, int id)
        {
            return users.Where(u => u.Id == id).AsQueryable();
        }
    }
}
