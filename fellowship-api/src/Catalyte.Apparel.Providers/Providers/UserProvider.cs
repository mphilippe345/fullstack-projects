using Catalyte.Apparel.Data.Interfaces;
using Catalyte.Apparel.Data.Models;
using Catalyte.Apparel.Providers.Interfaces;
using Catalyte.Apparel.Utilities;
using Catalyte.Apparel.Providers.Auth;
using Catalyte.Apparel.Utilities.HttpResponseExceptions;
using Microsoft.Extensions.Logging;
using System;
using System.Threading.Tasks;

namespace Catalyte.Apparel.Providers.Providers
{
    /// <summary>
    /// This class provides the implementation of the IUserProvider interface, providing service methods for users.
    /// </summary>
    public class UserProvider : IUserProvider
    {
        private readonly ILogger<UserProvider> _logger;
        private readonly IUserRepository _userRepository;
        private readonly GoogleAuthService googleAuthService = new();

        public UserProvider(
            IUserRepository userRepository,
            ILogger<UserProvider> logger
        )
        {
            _userRepository = userRepository;
            _logger = logger;
        }

        public async Task<User> GetUserByEmailAsync(string email)
        {
            User user;

            try
            {
                user = await _userRepository.GetUserByEmailAsync(email);
            }
            catch (Exception ex)
            {
                _logger.LogError(ex.Message);
                throw new ServiceUnavailableException("There was a problem connecting to the database.");
            }

            if (user == default)
            {
                _logger.LogError($"Could not find user with email: {email}");
                throw new NotFoundException($"Could not find user with email: {email}");
            }

            return user;
        }

        /// <summary>
        /// Retrieves user based on the id given
        /// </summary>
        /// <param name="id"></param>
        /// <returns></returns>
        /// <exception cref="ServiceUnavailableException"></exception>
        public async Task<User> GetUserByIdAsync(int id)
        {
            User user;

            try
            {
                user = await _userRepository.GetUserByIdAsync(id);
            }
            catch (Exception ex)
            {
                _logger.LogError(ex.Message);
                throw new ServiceUnavailableException("There was a problem connecting to the database.");
            }

            return user;
        }

        /// <summary>
        /// Persists updated user information given they have provided the correct credentials.
        /// </summary>
        /// <param name="bearerToken">String value in the "Authorization" property of the header.</param>
        /// <param name="id">Id of the user to update.</param>
        /// <param name="updatedUser">Updated user information to persist.</param>
        /// <returns>The updated user.</returns>
        public async Task<User> UpdateUserAsync(string bearerToken, int id, User updatedUser)
        {
            // AUTHENTICATES USER - SAME EMAIL, SAME PERSON
            // Authenticating the user ensures that the user is using Google to sign in
            string token = googleAuthService.GetTokenFromHeader(bearerToken);
            bool isAuthenticated = await googleAuthService.AuthenticateUserAsync(token, updatedUser);
            if (!isAuthenticated)
            {
                _logger.LogError("Email in the request body does not match email from the JWT Token");
                throw new BadRequestException("Email in the request body does not match email from JWT Token");
            }

            // VALIDATE USER TO UPDATE EXISTS
            User existingUser;

            try
            {
                existingUser = await _userRepository.GetUserByIdAsync(id);
            }
            catch (Exception ex)
            {
                _logger.LogError(ex.Message);
                throw new ServiceUnavailableException("There was a problem connecting to the database.");
            }

            if (existingUser == default)
            {
                _logger.LogInformation($"User with id: {id} does not exist.");
                throw new NotFoundException($"User with id:{id} not found.");
            }

            // TEMPORARY LOGIC TO PREVENT USER FROM UPDATING THEIR ROLE
            updatedUser.Role = existingUser.Role;

            // GIVE THE USER ID IF NOT SPECIFIED IN BODY TO AVOID DUPLICATE USERS
            if (updatedUser.Id == default)
                updatedUser.Id = id;

            // TIMESTAMP THE UPDATE
            updatedUser.DateModified = DateTime.UtcNow;
            updatedUser.DateCreated = existingUser.DateCreated;

            try
            {
                await _userRepository.UpdateUserAsync(updatedUser);
                _logger.LogInformation("User updated.");
            }
            catch (Exception ex)
            {
                _logger.LogError(ex.Message);
                throw new ServiceUnavailableException("There was a problem connecting to the database.");
            }

            return updatedUser;
        }

        /// <summary>
        /// Persists a user to the database given the provided email is not already in the database or null.
        /// </summary>
        /// <param name="newUser">The user to persist.</param>
        /// <returns>The user.</returns>
        public async Task<User> CreateUserAsync(User newUser)
        {
            if (newUser.Email == null)
            {
                _logger.LogError("User must have an email field.");
                throw new BadRequestException("User must have an email field");
            }

            // CHECK TO MAKE SURE THE USE EMAIL IS NOT TAKEN
            User existingUser;

            try
            {
                existingUser = await _userRepository.GetUserByEmailAsync(newUser.Email);
            }
            catch (Exception ex)
            {
                _logger.LogError(ex.Message);
                throw new ServiceUnavailableException("There was a problem connecting to the database.");
            }

            if (existingUser != default)
            {
                _logger.LogError("Email is taken.");
                throw new ConflictException("Email is taken");
            }

            // SET DEFAULT ROLE TO CUSTOMER AND TIMESTAMP
            newUser.Role = Constants.CUSTOMER;
            newUser.DateCreated = DateTime.UtcNow;
            newUser.DateModified = DateTime.UtcNow;

            User savedUser;

            try
            {
                savedUser = await _userRepository.CreateUserAsync(newUser);
                _logger.LogInformation("User saved.");
            }
            catch (Exception ex)
            {
                _logger.LogError(ex.Message);
                throw new ServiceUnavailableException("There was a problem connecting to the database.");
            }

            return savedUser;
        }
    }
}
