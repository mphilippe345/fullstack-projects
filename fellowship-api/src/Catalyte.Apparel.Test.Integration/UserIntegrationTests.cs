using Catalyte.Apparel.Data.Models;
using Catalyte.Apparel.DTOs;
using Catalyte.Apparel.Test.Integration.Utilities;
using Catalyte.Apparel.TestBase;
using Microsoft.AspNetCore.Mvc.Testing;
using Newtonsoft.Json;
using System.Net;
using System.Net.Http;
using System.Text;
using System.Threading.Tasks;
using Xunit;

namespace Catalyte.Apparel.Test.Integration
{
    [Collection("Sequential")]
    public class UserIntegrationTests : IClassFixture<CustomWebApplicationFactory>
    {
        private readonly HttpClient _client;
        User testUser;

        public UserIntegrationTests(CustomWebApplicationFactory factory)
        {
            _client = factory.CreateClient(new WebApplicationFactoryClientOptions
            {
                AllowAutoRedirect = false
            });


            testUser = UserHelpers.GenerateValidUser();
        }

        [Fact]
        public async Task CreateUser_ValidUser_Returns201()
        {
            // convert the user into a JSON string so we can post it
            var json = new StringContent(JsonConvert.SerializeObject(testUser), Encoding.UTF8, "application/json");
            
            // post it
            var response = await _client.PostAsync("/users", json);
            
            // map the results of the JSON body onto a UserDTO so we can inspect properties of it in the assert
            var content = await response.Content.ReadAsAsync<UserDTO>();
            
            Assert.Equal(HttpStatusCode.Created, response.StatusCode);
            Assert.Equal(testUser.Email, content.Email);
        }

        [Fact]
        public async Task CreateUser_DuplicateUser_Returns409()
        {
            testUser = UserHelpers.GenerateValidUser();
            var json = new StringContent(JsonConvert.SerializeObject(testUser), Encoding.UTF8, "application/json");
            await _client.PostAsync("/users", json);
            var response = await _client.PostAsync("/users", json);

            Assert.Equal(HttpStatusCode.Conflict, response.StatusCode);
        }
    }
}
