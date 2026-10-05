using System.Threading.Tasks;
using Catalyte.Apparel.Data.Models;

namespace Catalyte.Apparel.Providers.Interfaces
{
    /// <summary>
    /// This interface provides an abstraction layer for Review related service methods.
    /// </summary>
    public interface IReviewProvider
    {
        Task<Review> CreateReviewAsync(Review newReview);

        Task<Review> GetReviewByIdAsync(int id);

        Task DeleteReviewByIdAsync(int id);
    }
}