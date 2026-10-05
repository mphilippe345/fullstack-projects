using Catalyte.Apparel.Data.Models;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace Catalyte.Apparel.Providers.Interfaces
{
    /// <summary>
    /// This interface provides an abstraction layer for purchase related service methods.
    /// </summary>
    public interface IPurchaseProvider
    {
        Task<IEnumerable<Purchase>> GetPurchasesByEmailAsync(string email);

        Task<Purchase> CreatePurchaseAsync(Purchase model);

    }
}
