using Catalyte.Apparel.Data.Models;
using System.Threading.Tasks;

namespace Catalyte.Apparel.Data.Interfaces
{
    /// <summary>
    /// This interface provides an abstraction layer for user repository methods.
    /// </summary>
    public interface IUserRepository
    {
        Task<User> GetUserByIdAsync(int id);

        Task<User> GetUserByEmailAsync(string email);

        Task<User> CreateUserAsync(User user);

        Task<User> UpdateUserAsync(User user);
    }
}
