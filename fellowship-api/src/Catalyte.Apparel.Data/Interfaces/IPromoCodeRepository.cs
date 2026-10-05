using Catalyte.Apparel.Data.Models;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace Catalyte.Apparel.Data.Interfaces
{
    /// <summary>
    /// This interface provides an abstraction layer for PromoCode Repository methods.
    /// </summary>

    public interface IPromoCodeRepository
    {
        Task<PromoCode> CreatePromoCodeAsync(PromoCode promocode);

        Task<PromoCode> GetPromoCodeByTitleAsync(string title);
    }
}
