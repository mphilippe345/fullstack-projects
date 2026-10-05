using Catalyte.Apparel.Data.Context;
using Catalyte.Apparel.Data.Interfaces;
using Catalyte.Apparel.Data.Models;
using Microsoft.EntityFrameworkCore;
using System.Threading.Tasks;
using Catalyte.Apparel.Data.Filters;

namespace Catalyte.Apparel.Data.Repositories
{
    /// <summary>
    /// This class handles methods for making requests to the PromoCodeRepository.
    /// </summary>
    public class ReviewRepository : IReviewRepository
    {
        private readonly IApparelCtx _ctx;

        public ReviewRepository(IApparelCtx ctx)
        {
            _ctx = ctx;
        }

        public async Task<Review> CreateReviewAsync(Review review)
        {
            _ctx.Reviews.Add(review);
            await _ctx.SaveChangesAsync();

            return review;
        }

        public async Task<Review> GetReviewByIdAsync(int id)
        {
            return await _ctx.Reviews
                .AsNoTracking()
                .WhereIdEquals(id)
                .SingleOrDefaultAsync();
        }

        public async Task DeleteReviewByIdAsync(Review review)
        {
            _ctx.Reviews.Remove(review);
            await _ctx.SaveChangesAsync();
        }
    }
}