using Catalyte.Apparel.Test.Integration.Utilities;
using Microsoft.AspNetCore.Mvc.Testing;
using System.Net;
using System.Net.Http;
using System.Threading.Tasks;
using Xunit;

namespace Catalyte.Apparel.Test.Integration
{
    [Collection("Sequential")]
    public class PurchaseIntegrationTests : IClassFixture<CustomWebApplicationFactory>
    {
        private readonly HttpClient _client;

        public PurchaseIntegrationTests(CustomWebApplicationFactory factory)
        {
            _client = factory.CreateClient(new WebApplicationFactoryClientOptions
            {
                AllowAutoRedirect = false
            });
        }

        [Fact]
        public async Task PurchaseEndpointError_Returns404()
        {
            var response = await _client.GetAsync("/purchases");
            Assert.Equal(HttpStatusCode.NotFound, response.StatusCode);
        }

        [Fact]
        public async Task GetPurchasesByEmail_Returns200OK()
        {
            var response = await _client.GetAsync("/purchases/email");
            Assert.Equal(HttpStatusCode.OK, response.StatusCode);
        }
    }
}
