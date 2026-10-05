using System;
using System.Collections.Generic;

namespace Catalyte.Apparel.Data.Models
{
    /// <summary>
    /// Describes the object for the delivery address of the purchase.
    /// </summary>
    public class DeliveryAddress : BaseEntity
    {
        public string FirstName { get; set; }

        public string LastName { get; set; }

        public string DeliveryStreet { get; set; }

        public string DeliveryStreet2 { get; set; }

        public string DeliveryCity { get; set; }

        public string DeliveryState { get; set; }

        public int DeliveryZip { get; set; }

        private sealed class DeliveryAddressEqualityComparer : IEqualityComparer<DeliveryAddress>
        {
            public bool Equals(DeliveryAddress x, DeliveryAddress y)
            {
                if (ReferenceEquals(x, y)) return true;
                if (ReferenceEquals(x, null)) return false;
                if (ReferenceEquals(y, null)) return false;
                if (x.GetType() != y.GetType()) return false;
                return x.FirstName == y.FirstName && x.LastName == y.LastName && x.DeliveryStreet == y.DeliveryStreet && x.DeliveryStreet2 == y.DeliveryStreet2 && x.DeliveryCity == y.DeliveryCity && x.DeliveryState == y.DeliveryState && x.DeliveryZip == y.DeliveryZip;
            }

            public int GetHashCode(DeliveryAddress obj)
            {
                return HashCode.Combine(obj.FirstName, obj.LastName, obj.DeliveryStreet, obj.DeliveryStreet2, obj.DeliveryCity, obj.DeliveryState, obj.DeliveryZip);
            }
        }

        public static IEqualityComparer<DeliveryAddress> DeliveryAddressComparer { get; } = new DeliveryAddressEqualityComparer();
    }

}
