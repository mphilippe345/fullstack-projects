using Catalyte.Apparel.Data.Interfaces;
using Catalyte.Apparel.Data.Models;
using Catalyte.Apparel.Providers.Interfaces;
using Catalyte.Apparel.Providers.Providers;
using Microsoft.Extensions.Logging;
using Moq;
using System.Collections.Generic;
using Xunit;

namespace Catalyte.Apparel.Test.Unit
{
    public class PurchaseUnitTests
    {
        private readonly List<Purchase> purchases;
        private readonly IPurchaseProvider purchaseProvider;
        private readonly Mock<IPurchaseRepository> purchaseRepo;
        private readonly Mock<ILogger<PurchaseProvider>> logger;
        private readonly IProductProvider productProvider;

        public PurchaseUnitTests()
        {
            productProvider = null;
            purchaseRepo = new Mock<IPurchaseRepository>();
            logger = new Mock<ILogger<PurchaseProvider>>();
            purchaseProvider = new PurchaseProvider(purchaseRepo.Object, logger.Object, productProvider);
            purchases = new List<Purchase>()
            {
                  new Purchase()
                  {
                    BillingStreet = "123 Main",
                    BillingStreet2 = "Apt A",
                    BillingCity = "Atlanta",
                    BillingState = "GA",
                    BillingZip = "31675",
                    BillingEmail = "customer@home.com",
                    BillingPhone = "(714) 345-8765",
                    DeliveryFirstName = "Max",
                    DeliveryLastName = "Space",
                    DeliveryStreet = "123 Hickley",
                    DeliveryStreet2 = null,
                    DeliveryCity = "Birmingham",
                    DeliveryState = "AL",
                    DeliveryZip = "43690",
                    CardNumber = "1435678998761234",
                    CVV = "456",
                    Expiration = "11/21",
                    CardHolder = ""
                   },
                  new Purchase()
                  {
                    BillingStreet = "123 Main",
                    BillingStreet2 = "Apt A",
                    BillingCity = "Atlanta",
                    BillingState = "GA",
                    BillingZip = "31675",
                    BillingEmail = "customer@home.com",
                    BillingPhone = "(714) 345-8765",
                    DeliveryFirstName = "Max",
                    DeliveryLastName = "Space",
                    DeliveryStreet = "123 Hickley",
                    DeliveryStreet2 = null,
                    DeliveryCity = "Birmingham",
                    DeliveryState = "AL",
                    DeliveryZip = "43690",
                    CardNumber = "1435678998761234",
                    CVV = "456",
                    Expiration = "11/21",
                    CardHolder = ""
                   }
            };
           
        }
        [Fact]
        public void GetAllPurchasesAsync_ReturnsAllPurchases()
        {
            var email = "customer@home.com";
            purchaseRepo.Setup(m => m.GetPurchasesByEmailAsync(email)).ReturnsAsync(purchases);
            var expected = purchases;
            var actual = purchaseProvider.GetPurchasesByEmailAsync(email).Result;
            Assert.Equal(expected, actual);
        }
    }
 }