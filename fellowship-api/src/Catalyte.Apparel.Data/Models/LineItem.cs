using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;

namespace Catalyte.Apparel.Data.Models
{
    /// <summary>
    /// Describes one line item of a purchase.
    /// </summary>
    public class LineItem : BaseEntity
    {
        [Column("PurchaseID")]
        public int PurchaseId { get; set; }

        [Column("ProductID")]
        public int ProductId { get; set; }

        public int Quantity { get; set; }

        public Purchase Purchase { get; set; }

        public Product Product { get; set; }


        private sealed class PurchaseIdProductIdQuantityEqualityComparer : IEqualityComparer<LineItem>
        {
            public bool Equals(LineItem x, LineItem y)
            {
                if (ReferenceEquals(x, y)) return true;
                if (ReferenceEquals(x, null)) return false;
                if (ReferenceEquals(y, null)) return false;
                if (x.GetType() != y.GetType()) return false;
                return x.PurchaseId == y.PurchaseId && x.ProductId == y.ProductId && x.Quantity == y.Quantity;
            }

            public int GetHashCode(LineItem obj)
            {
                return HashCode.Combine(obj.PurchaseId, obj.ProductId, obj.Quantity);
            }
        }
        public static IEqualityComparer<LineItem> PurchaseIdProductIdQuantityComparer { get; } = new PurchaseIdProductIdQuantityEqualityComparer();
    }
}
