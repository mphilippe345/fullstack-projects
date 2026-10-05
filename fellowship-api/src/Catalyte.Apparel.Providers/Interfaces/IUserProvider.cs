using Catalyte.Apparel.Data.Models;
using System.Threading.Tasks;

namespace Catalyte.Apparel.Providers.Interfaces
{
    /// <summary>
    /// This interface provides an abstraction layer for user related service methods.
    /// </summary>
    public interface IUserProvider
    {
        Task<User> GetUserByEmailAsync(string email);

        Task<User> GetUserByIdAsync(int id);

        Task<User> UpdateUserAsync(string credentials, int id, User user);

        Task<User> CreateUserAsync(User user);
    }
}
