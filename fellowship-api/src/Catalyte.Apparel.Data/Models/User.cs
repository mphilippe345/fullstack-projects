using Newtonsoft.Json;
using System;
using System.Collections.Generic;

namespace Catalyte.Apparel.Data.Models
{
    /// <summary>
    /// Describes a sports apparel site user.
    /// </summary>
    public class User : BaseEntity
    {

        [JsonRequired]
        public string Email { get; set; }

        public string Role { get; set; }

        public string FirstName { get; set; }

        public string LastName { get; set; }

        public string street { get; set; }

        public string street2 { get; set; }

        public string city { get; set; }

        public string state { get; set; }

        public string zip { get; set; }

        public ICollection<WishListItem> WishList { get; set; }

        public override string ToString()
        {
            return JsonConvert.SerializeObject(this);
        }

        public static IEqualityComparer<User> ProductComparer { get; } = new ProductEqualityComparer();
        
        private sealed class ProductEqualityComparer : IEqualityComparer<User>
        {
            public bool Equals(User x, User y)
            {
                if (ReferenceEquals(x, y)) return true;
                if (ReferenceEquals(x, null)) return false;
                if (ReferenceEquals(y, null)) return false;
                if (x.GetType() != y.GetType()) return false;
                return x.Email == y.Email && x.Role == y.Role && x.FirstName == y.FirstName && x.LastName == y.LastName;
            }

            public int GetHashCode(User obj)
            {
                var hashCode = new HashCode();
                hashCode.Add(obj.Email);
                hashCode.Add(obj.Role);
                hashCode.Add(obj.FirstName);
                hashCode.Add(obj.LastName);
                return hashCode.ToHashCode();
            }
        }
    }
}
