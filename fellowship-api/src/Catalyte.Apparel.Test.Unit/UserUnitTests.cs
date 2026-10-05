using Catalyte.Apparel.Data.Interfaces;
using Catalyte.Apparel.Data.Models;
using Catalyte.Apparel.Providers.Interfaces;
using Catalyte.Apparel.Providers.Providers;
using Catalyte.Apparel.Utilities.HttpResponseExceptions;
using FluentAssertions;
using Microsoft.Extensions.Logging;
using Moq;
using System;
using System.Threading.Tasks;
using Xunit;

namespace Catalyte.Apparel.Test.Unit
{
    public class UserUnitTests
    {
        private readonly IUserProvider userProvider;
        private readonly Mock<IUserRepository> userRepo;
        private readonly Mock<ILogger<UserProvider>> logger;

        User testUser;

        public UserUnitTests()
        {
            userRepo = new Mock<IUserRepository>();
            logger = new Mock<ILogger<UserProvider>>();
            userProvider = new UserProvider(userRepo.Object, logger.Object);

            testUser = generateValidUser();

            userRepo
                .Setup(m => m.CreateUserAsync(It.IsAny<User>()))
                .ReturnsAsync(testUser);

            userRepo
                .Setup(m => m.GetUserByEmailAsync(It.IsAny<string>()))
                .ReturnsAsync(() => null);

        }

        private User generateValidUser()
        {
            return new User
            {
                Email = "bob@ross.com"
            };

        }

        [Fact]
        public void CreateUserHappyPath()
        {
            var result = userProvider.CreateUserAsync(testUser).Result;

            Assert.Equal(testUser, result);
        }

        [Fact]
        public async Task CreateUser_NoEmail_Returns400BadRequest()
        {
            testUser.Email = null;
            Func<Task> result = async () => { await userProvider.CreateUserAsync(testUser); };
            await result.Should().ThrowAsync<BadRequestException>();
        }

        [Fact]
        public async Task CreateUser_DatabaseUnavailable_Returns503ServiceUnavailable()
        {
            userRepo
                .Setup(m => m.GetUserByEmailAsync(It.IsAny<string>()))
                .ThrowsAsync(new Exception());
            Func<Task> result = async () => { await userProvider.CreateUserAsync(testUser); };
            await result.Should().ThrowAsync<ServiceUnavailableException>();
        }

        [Fact]
        public async Task CreateUser_EmailAlreadyExists_Returns409Conflict()
        {
            userRepo
                .Setup(m => m.GetUserByEmailAsync(It.IsAny<string>()))
                .ReturnsAsync(testUser);
            Func<Task> result = async () => { await userProvider.CreateUserAsync(testUser); };
            await result.Should().ThrowAsync<ConflictException>();
        }

        [Fact]
        public async Task CreateUser_DatabaseUnavailableForSave_Returns503ServiceUnavailable()
        {
            userRepo
                .Setup(m => m.CreateUserAsync(It.IsAny<User>()))
                .ThrowsAsync(new Exception());
            Func<Task> result = async () => { await userProvider.CreateUserAsync(testUser); };
            await result.Should().ThrowAsync<ServiceUnavailableException>();
        }

    }
}
