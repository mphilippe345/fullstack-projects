using System;
using System.Collections.Generic;

namespace Catalyte.Apparel.Data.Models
{
    /// <summary>
    /// Describes the object for the billing address of the purchase.
    /// </summary>
    public class BillingAddress : BaseEntity
    {
        public string BillingStreet { get; set; }

        public string BillingStreet2 { get; set; }

        public string BillingCity { get; set; }

        public string BillingState { get; set; }

        public string BillingZip { get; set; }

        public string Email { get; set; }

        public string Phone { get; set; }

        sealed class BillingAddressEqualityComparer : IEqualityComparer<BillingAddress>
        {
            public bool Equals(BillingAddress x, BillingAddress y)
            {
                if (ReferenceEquals(x, y)) return true;
                if (ReferenceEquals(x, null)) return false;
                if (ReferenceEquals(y, null)) return false;
                if (x.GetType() != y.GetType()) return false;
                return x.BillingStreet == y.BillingStreet && x.BillingStreet2 == y.BillingStreet2 && x.BillingCity == y.BillingCity && x.BillingState == y.BillingState && x.BillingZip == y.BillingZip && x.Email == y.Email && x.Phone == y.Phone;
            }

            public int GetHashCode(BillingAddress obj)
            {
                return HashCode.Combine(obj.BillingStreet, obj.BillingStreet2, obj.BillingCity, obj.BillingState, obj.BillingZip, obj.Email, obj.Phone);
            }
        }

        public static IEqualityComparer<BillingAddress> BillingAddressComparer { get; set; } = new BillingAddressEqualityComparer();

    }
}
