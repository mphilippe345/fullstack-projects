using Catalyte.Apparel.Data.Context;
using Catalyte.Apparel.Data.Interfaces;
using Catalyte.Apparel.Data.Models;
using Microsoft.EntityFrameworkCore;
using System.Threading.Tasks;
using System.Collections.Generic;
using System.Linq;
using Catalyte.Apparel.Data.Filters;

namespace Catalyte.Apparel.Data.Repositories
{
    /// <summary>
    /// This class handles methods for making requests to the PromoCodeRepository.
    /// </summary>
    public class PromoCodeRepository : IPromoCodeRepository
    {
        private readonly IApparelCtx _ctx;

        public PromoCodeRepository(IApparelCtx ctx)
        {
            _ctx = ctx;
        }

        public async Task<PromoCode> CreatePromoCodeAsync(PromoCode promocode)
        {
            _ctx.PromoCodes.Add(promocode);
            await _ctx.SaveChangesAsync();

            return promocode;
        }

        public async Task<PromoCode> GetPromoCodeByTitleAsync(string title)
        {
            return await _ctx.PromoCodes
                .AsNoTracking()
                .WherePromoCodeTitleEquals(title)
                .SingleOrDefaultAsync();
        }
    }
}