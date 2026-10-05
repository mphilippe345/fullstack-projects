using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using Newtonsoft.Json;

namespace Catalyte.Apparel.Data.Models
{
    /// <summary>
    /// Describes a purchase object that holds the information for a transaction.
    /// </summary>
    public class Purchase : BaseEntity
    {
        public DateTime OrderDate { get; set; }

        [MaxLength(100)]
        public string BillingStreet { get; set; }

        [MaxLength(100)]
        public string BillingStreet2 { get; set; }

        [MaxLength(50)]
        public string BillingCity { get; set; }

        [MaxLength(35)]
        public string BillingState { get; set; }

        [MaxLength(10)]
        public string BillingZip { get; set; }

        [MaxLength(100)]
        public string BillingEmail { get; set; }

        [MaxLength(15)]
        public string BillingPhone { get; set; }

        [MaxLength(50)]
        public string DeliveryFirstName { get; set; }

        [MaxLength(50)]
        public string DeliveryLastName { get; set; }

        [MaxLength(100)]
        public string DeliveryStreet { get; set; }

        [MaxLength(100)]
        public string DeliveryStreet2 { get; set; }

        [MaxLength(50)]
        public string DeliveryCity { get; set; }

        [MaxLength(35)]
        public string DeliveryState { get; set; }

        [MaxLength(10)]
        public string DeliveryZip { get; set; }

        [MaxLength(16)]
        public string CardNumber { get; set; }

        public string CVV { get; set; }

        [MaxLength(5)]
        public string Expiration { get; set; }

        [MaxLength(100)]
        public string CardHolder { get; set; }

        public ICollection<LineItem> LineItems { get; set; }

        public decimal SubTotal { get; set; }

        public decimal Shipping { get; set; }

        public decimal Total { get; set; }

        public override string ToString()
        {
            return JsonConvert.SerializeObject(this);
        }
        public static IEqualityComparer<Purchase> ProductComparer { get; } = new ProductEqualityComparer();

        private sealed class ProductEqualityComparer : IEqualityComparer<Purchase>
        {
            public bool Equals(Purchase x, Purchase y)
            {
                if (ReferenceEquals(x, y)) return true;
                if (ReferenceEquals(x, null)) return false;
                if (ReferenceEquals(y, null)) return false;
                if (x.GetType() != y.GetType()) return false;
                return x.OrderDate == y.OrderDate && x.Total == y.Total && x.LineItems == y.LineItems && x.BillingEmail == y.BillingEmail;
            }

            public int GetHashCode(Purchase obj)
            {
                var hashCode = new HashCode();
                hashCode.Add(obj.OrderDate);
                hashCode.Add(obj.Total);
                hashCode.Add(obj.LineItems);
                hashCode.Add(obj.BillingEmail);
                return hashCode.ToHashCode();
            }
        }
    }
}
