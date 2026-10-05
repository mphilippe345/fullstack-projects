using Catalyte.Apparel.Data.Models;
using System;
using System.Linq;

namespace Catalyte.Apparel.TestBase
{
    public static class UserHelpers
    {

        public static User GenerateValidUser()
        {
            return new User
            {
                Email = GenerateRandomEmail()
            };
        }

        public static string GenerateRandomCharacterString(int length)
        {
            Random random = new Random();

            const string chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

            return new string(Enumerable.Repeat(chars, length).Select(s => s[random.Next(s.Length)]).ToArray());
        }

        public static string GenerateRandomEmail()
        {
            var preAt = GenerateRandomCharacterString(10);
            var postAt = GenerateRandomCharacterString(10);
            var dotEnd = ".com";

            return preAt + "@" + postAt + dotEnd;
        }

    }

}

