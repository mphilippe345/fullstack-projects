using Catalyte.Apparel.Test.Integration.Utilities;
using Microsoft.AspNetCore.Mvc.Testing;
using System.Net;
using System.Net.Http;
using System.Threading.Tasks;
using Xunit;
using Catalyte.Apparel.Data.Models;
using Newtonsoft.Json;
using System.Text;

namespace Catalyte.Apparel.Test.Integration
{
    [Collection("Sequential")]
    public class PromoIntegrationTests : IClassFixture<CustomWebApplicationFactory>
    {
        private readonly HttpClient _client;
        private readonly PromoCode ValidPromo;
        private readonly PromoCode InvalidTitlepromo;
        private readonly PromoCode InvalidDescriptionPromo;
        private readonly PromoCode InvalidTypePromo;
        private readonly PromoCode InvalidRatePromo_percent;
        private readonly PromoCode InvalidRatePromo_Flat;
        public PromoIntegrationTests(CustomWebApplicationFactory factory)
        {
            _client = factory.CreateClient(new WebApplicationFactoryClientOptions
            {
                AllowAutoRedirect = false
            });

            ValidPromo = new PromoCode()
            {
                title = "summer2015",
                description = "dfisjnfjd",
                type = "$",
                rate = 10
            };

            InvalidTitlepromo = new PromoCode()
            {
                title = "",
                description = "dfisjnfjd",
                type = "$",
                rate = 10
            };

            InvalidDescriptionPromo = new PromoCode()
            {
                title = "summer2015",
                description = "",
                type = "$",
                rate = 10
            };

            InvalidTypePromo = new PromoCode()
            {
                title = "summer2015",
                description = "dfisjnfjd",
                type = "",
                rate = 10
            };

            InvalidRatePromo_percent = new PromoCode()
            {

                title = "summer2015",
                description = "dfisjnfjd",
                type = "%",
                rate = 150
            };

            InvalidRatePromo_Flat = new PromoCode()
            {
                title = "summer2015",
                description = "dfisjnfjd",
                type = "$",
                rate = 0
            };
        }

        [Fact]
        public async Task CreatePromo_Returns201Created_ValidPromo()
        {
            var stringContent = new StringContent(JsonConvert.SerializeObject(ValidPromo), Encoding.UTF8, "application/json");
            var response = await _client.PostAsync("/promos", stringContent);
            Assert.Equal(HttpStatusCode.Created, response.StatusCode);
        }

        [Fact]
        public async Task CreatePromo_400BadRequest_InvalidTitlePromo()
        {
            var stringContent = new StringContent(JsonConvert.SerializeObject(InvalidTitlepromo), Encoding.UTF8, "application/json");
            var response = await _client.PostAsync("/promos", stringContent);
            Assert.Equal(HttpStatusCode.BadRequest, response.StatusCode);
        }

        [Fact]
        public async Task CreatePromo_400BadRequest_InvalidDescriptionPromo()
        {
            var stringContent = new StringContent(JsonConvert.SerializeObject(InvalidDescriptionPromo), Encoding.UTF8, "application/json");
            var response = await _client.PostAsync("/promos", stringContent);
            Assert.Equal(HttpStatusCode.BadRequest, response.StatusCode);
        }

        [Fact]
        public async Task CreatePromo_400BadRequest_InvalidTypePromo()
        {
            var stringContent = new StringContent(JsonConvert.SerializeObject(InvalidTypePromo), Encoding.UTF8, "application/json");
            var response = await _client.PostAsync("/promos", stringContent);
            Assert.Equal(HttpStatusCode.BadRequest, response.StatusCode);
        }

        [Fact]
        public async Task CreatePromo_400BadRequest_InvalidRatePromo_Percent()
        {
            var stringContent = new StringContent(JsonConvert.SerializeObject(InvalidRatePromo_percent), Encoding.UTF8, "application/json");
            var response = await _client.PostAsync("/promos", stringContent);
            Assert.Equal(HttpStatusCode.BadRequest, response.StatusCode);
        }

        [Fact]
        public async Task CreatePromo_400BadRequest_InvalidRatePromo_Flat()
        {
            var stringContent = new StringContent(JsonConvert.SerializeObject(InvalidRatePromo_Flat), Encoding.UTF8, "application/json");
            var response = await _client.PostAsync("/promos", stringContent);
            Assert.Equal(HttpStatusCode.BadRequest, response.StatusCode);
        }
    }
}
