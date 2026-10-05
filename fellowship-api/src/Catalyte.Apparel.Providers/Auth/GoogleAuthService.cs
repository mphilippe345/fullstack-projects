using Catalyte.Apparel.Utilities.HttpResponseExceptions;
using Catalyte.Apparel.Data.Models;
using Google.Apis.Auth;
using System;
using System.Threading.Tasks;

namespace Catalyte.Apparel.Providers.Auth
{
    /// <summary>
    /// This class provides tools to complete backend authorization of Google JWT tokes.
    /// </summary>
    public class GoogleAuthService
    {
        /// <summary>
        /// Parses authorization header value and returns the token.
        /// </summary>
        /// <param name="bearerToken">Authorization header value</param>
        /// <returns>Token string.</returns>
        public string GetTokenFromHeader(string bearerToken)
        {
            // PARSE JWT TOKEN
            if (bearerToken != null && bearerToken.StartsWith("Bearer "))
            {
                return bearerToken.Substring(7);
            }
            else
            {
                throw new BadRequestException("Authorization Header must start with 'Bearer '.");
            }
        }

        /// <summary>
        /// Helper method used to validate and get user information from JWT token.
        /// </summary>
        /// <param name="idToken">Token to validate.</param>
        /// <returns>Validated user information from JWT token.</returns>
        private async Task<GoogleJsonWebSignature.Payload> ValidateIdTokenAndGetUserInfoAsync(string idToken)
        {
            if (string.IsNullOrWhiteSpace(idToken))
            {
                return null;
            }

            try
            {
                return await GoogleJsonWebSignature.ValidateAsync(idToken);
            }
            catch (Exception ex)
            {
                throw new BadRequestException(ex.Message);
            }
        }

        /// <summary>
        /// Provides .NET implementation of Google backend authentication.
        /// 
        /// SEE DOCUMENTATION: https://developers.google.com/identity/sign-in/web/backend-auth
        /// </summary>
        /// <param name="idToken">JWT Token</param>
        /// <param name="user">User to validate.</param>
        /// <returns>Boolean if the user has been validated.</returns>
        public async Task<bool> AuthenticateUserAsync(string token, User user)
        {
            var userInfo = await ValidateIdTokenAndGetUserInfoAsync(token);

            // GET EMAIL FROM GOOGLE TOKEN
            string googleEmail = userInfo.Email;

            // VERIFY TOKEN IS NOT NULL
            if (string.IsNullOrWhiteSpace(googleEmail))
            {
                return false;
            }

            // AUTHENTICATE USER
            return googleEmail.Equals(user.Email);
        }
    }
}

