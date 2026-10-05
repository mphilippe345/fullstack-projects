using Catalyte.Apparel.Data.Context;
using Catalyte.Apparel.Data.Interfaces;
using Catalyte.Apparel.Data.Models;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Logging;
using System.Collections.Generic;
using System.Threading.Tasks;
using Catalyte.Apparel.Data.Filters;

namespace Catalyte.Apparel.Data.Repositories
{
    /// <summary>
    /// This class handles methods for making requests to the purchase repository.
    /// </summary>
    public class PurchaseRepository : IPurchaseRepository
    {
        private readonly ILogger<PurchaseRepository> _logger;
        private readonly IApparelCtx _ctx;

        public PurchaseRepository(ILogger<PurchaseRepository> logger, IApparelCtx ctx)
        {
            _logger = logger;
            _ctx = ctx;
        }
        /// <summary>
        /// This method gets all purchase objects associated with a given email
        /// </summary>
        /// <param name="email"> email to be filtered by</param>
        /// <returns>all purchase objects associated withthe email</returns>
        public async Task<List<Purchase>> GetPurchasesByEmailAsync(string email)
        {
            return await _ctx.Purchases
             .WherePurchaseEmailEquals(email)
             .Include(p => p.LineItems)
             .ThenInclude(p => p.Product)
             .AsNoTracking()
             .ToListAsync();
        }

        public async Task<Purchase> CreatePurchaseAsync(Purchase purchase)
        {
            await _ctx.Purchases.AddAsync(purchase);
            await _ctx.SaveChangesAsync();
            return purchase;
        }
    }
}
