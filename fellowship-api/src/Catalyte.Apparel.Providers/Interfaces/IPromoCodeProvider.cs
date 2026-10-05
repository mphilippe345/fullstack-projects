using System.Threading.Tasks;
using Catalyte.Apparel.Data.Models;
using System.Collections.Generic;

namespace Catalyte.Apparel.Providers.Interfaces
{
    /// <summary>
    /// This interface provides an abstraction layer for PromoCode related service methods.
    /// </summary>
    public interface IPromoCodeProvider
    {
        Task<PromoCode> CreatePromoCodeAsync(PromoCode promoCode);

        Task<PromoCode> GetPromoCodeByTitleAsync(string title);
    }
}
