using Catalyte.Apparel.Data.Interfaces;
using Catalyte.Apparel.Data.Models;
using Catalyte.Apparel.Providers.Interfaces;
using Catalyte.Apparel.Utilities.HttpResponseExceptions;
using Microsoft.Extensions.Logging;
using System;
using System.Collections.Generic;
using System.Threading.Tasks;
using System.Linq;
using System.Text;
using Catalyte.Apparel.Utilities;

namespace Catalyte.Apparel.Providers.Providers
{
    /// <summary>
    /// This class provides the implementation of the IPurchaseProvider interface, providing service methods for purchases.
    /// </summary>
    public class PurchaseProvider : IPurchaseProvider
    {
        private readonly ILogger<PurchaseProvider> _logger;
        private readonly IPurchaseRepository _purchaseRepository;
        private readonly IProductProvider _productProvider;

        public PurchaseProvider(IPurchaseRepository purchaseRepository, ILogger<PurchaseProvider> logger, IProductProvider productProvider)
        {
            _logger = logger;
            _purchaseRepository = purchaseRepository;
            _productProvider = productProvider;
        }

        /// <summary>
        /// Retrieves all purchases from the database.
        /// </summary>
        /// <returns>All purchases.</returns>
        public async Task<IEnumerable<Purchase>> GetPurchasesByEmailAsync(string email)
        {
            List<Purchase> purchases;

            try
            {
                purchases = await _purchaseRepository.GetPurchasesByEmailAsync(email);

            }
            catch (Exception ex)
            {
                _logger.LogError(ex.Message);
                throw new ServiceUnavailableException("There was a problem connecting to the database.");
            }

            return purchases;
        }

        /// <summary>
        /// Persists a purchase to the database. Server does not allow inactive purchases to proceed. A 422 status code, 
        /// with a payload that informs the client about which products were inactive is displayed.
        /// </summary>
        /// <param name="newPurchase">Purchase model used to build the purchase.</param>
        /// <returns>The persisted purchase with IDs.</returns>
        public async Task<Purchase> CreatePurchaseAsync(Purchase newPurchase)
        {
            Purchase savedPurchase;

            List<Exception> exceptions;
            exceptions = CreditCardValidator.ValidateCreditCard(newPurchase);
            if (exceptions != null && exceptions.Any())
            {
                var aggregateExceptions = new AggregateException("Incorrect card information:", exceptions);
                throw new BadRequestException(aggregateExceptions.Message);
            }
            var lineItems = newPurchase.LineItems;
            List<string> inactiveProductNames = new();
            foreach (var lineItem in lineItems)
            {
                var product = await _productProvider.GetProductByIdAsync(lineItem.ProductId);
                if (!product.Active)
                {
                    inactiveProductNames.Add(product.Name);
                }

                newPurchase.SubTotal += lineItem.Quantity * product.Price + 0.00m;
                if (newPurchase.DeliveryState == "Hawaii" || newPurchase.DeliveryState == "Alaska")
                {

                    if (newPurchase.SubTotal >= 50.00m)
                    {
                        newPurchase.Shipping = Constants.HiAkOverFiftyShipping;
                    }
                    if (newPurchase.SubTotal < 50.00m)
                    {
                        newPurchase.Shipping = Constants.HiAkUnderFiftyShipping;
                    }
                }
                else
                {
                    if (newPurchase.SubTotal >= 50.00m)
                    {
                        newPurchase.Shipping = Constants.LowerFortyEightOverFiftyShipping;
                    }
                    if (newPurchase.SubTotal < 50.00m)
                    {
                        newPurchase.Shipping = Constants.LowerFortyEightUnderFiftyShipping;
                    }
                }
            }
            newPurchase.Total = newPurchase.SubTotal + newPurchase.Shipping + 0.00m;


            if (inactiveProductNames.Any())
            {
                string message = inactiveProductNames.Count == 1 ?
                    "The following product is inactive and is unable to be purchased: " :
                    "The following products are inactive and are unable to be purchased: ";
                var builder = new StringBuilder();
                builder.Append(message);
                string nameList = string.Join(", ", inactiveProductNames);
                builder.Append(nameList);
                var fullErrorMessage = builder.ToString();
                throw new UnprocessableEntity(fullErrorMessage);
            }
            try
            {
                savedPurchase = await _purchaseRepository.CreatePurchaseAsync(newPurchase);
            }
            catch (Exception ex)
            {
                _logger.LogError(ex.Message);
                throw new ServiceUnavailableException("There was a problem connecting to the database.");
            }
            return savedPurchase;
        }
    }
}
