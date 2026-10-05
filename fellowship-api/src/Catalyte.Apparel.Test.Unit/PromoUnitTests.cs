using Catalyte.Apparel.Data.Interfaces;
using Catalyte.Apparel.Data.Models;
using Catalyte.Apparel.Providers.Interfaces;
using Catalyte.Apparel.Providers.Providers;
using Microsoft.Extensions.Logging;
using Moq;
using Xunit;

namespace Catalyte.Apparel.Test.Unit
{
    public class PromoUnitTests
    {
        private readonly PromoCode promo;
        private readonly IPromoCodeProvider promoProvider;
        private readonly Mock<IPromoCodeRepository> promoRepo;
        private readonly Mock<ILogger<PromoCodeProvider>> logger;

        public PromoUnitTests()
        {
            promoRepo = new Mock<IPromoCodeRepository>();
            logger = new Mock<ILogger<PromoCodeProvider>>();
            promoProvider = new PromoCodeProvider(promoRepo.Object, logger.Object);

            promo = new PromoCode()
            {
                title = "summer2015",
                description = "dfisjnfjd",
                type = "$",
                rate = 10
            };
        }

        [Fact]
        public void CreatePromoAsync_ValidPromo_RetutrnsPromo()
        {
            promoRepo.Setup(m => m.CreatePromoCodeAsync(promo)).ReturnsAsync(promo);
            var expected = promo;
            var actual = promoProvider.CreatePromoCodeAsync(promo).Result;
            Assert.Equal(expected, actual);
        }
    }
}
