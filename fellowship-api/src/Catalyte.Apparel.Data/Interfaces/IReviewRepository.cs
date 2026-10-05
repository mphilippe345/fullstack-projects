using Catalyte.Apparel.Data.Models;
using System.Threading.Tasks;

namespace Catalyte.Apparel.Data.Interfaces
{
    /// <summary>
    /// This interface provides an abstraction layer for PromoCode Repository methods.
    /// </summary>

    public interface IReviewRepository
    {
        Task<Review> CreateReviewAsync(Review review);

        Task<Review> GetReviewByIdAsync(int id);

        Task DeleteReviewByIdAsync(Review review);
    }
}