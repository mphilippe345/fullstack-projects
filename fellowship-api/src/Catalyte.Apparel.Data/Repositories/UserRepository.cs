using Catalyte.Apparel.Data.Context;
using Catalyte.Apparel.Data.Filters;
using Catalyte.Apparel.Data.Interfaces;
using Catalyte.Apparel.Data.Models;
using Microsoft.EntityFrameworkCore;
using System.Threading.Tasks;

namespace Catalyte.Apparel.Data.Repositories
{
    /// <summary>
    /// This class handles methods for making requests to the user repository.
    /// </summary>
    public class UserRepository : IUserRepository
    {
        private readonly IApparelCtx _ctx;

        public UserRepository(IApparelCtx ctx)
        {
            _ctx = ctx;
        }

        public async Task<User> GetUserByIdAsync(int id)
        {
            return await _ctx.Users
                .Include(u => u.WishList)
                .ThenInclude(p => p.Product)
                .AsNoTracking()
                .WhereUserIdEquals(id)
                .SingleOrDefaultAsync();
        }

        public async Task<User> GetUserByEmailAsync(string email)
        {
            return await _ctx.Users
                .Include(u => u.WishList)
                .ThenInclude(p => p.Product)
                .AsNoTracking()
                .WhereUserEmailEquals(email)
                .SingleOrDefaultAsync();
        }

        public async Task<User> CreateUserAsync(User user)
        {
            _ctx.Users.Add(user);
            await _ctx.SaveChangesAsync();

            return user;
        }

        public async Task<User> UpdateUserAsync(User user)
        {
            _ctx.Users.Update(user);
            await _ctx.SaveChangesAsync();

            return user;
        }
    }
}
