using System;
using System.Collections.Generic;

namespace Catalyte.Apparel.Data.Models
{
    /// <summary>
    /// Describes the credit card object for transactions.
    /// </summary>
    public class CreditCard
    {
        public string CardNumber { get; set; }

        public string CVV { get; set; }

        public string Expiration { get; set; }

        public string CardHolder { get; set; }

        private sealed class CreditCardEqualityComparer : IEqualityComparer<CreditCard>
        {
            public bool Equals(CreditCard x, CreditCard y)
            {
                if (ReferenceEquals(x, y)) return true;
                if (ReferenceEquals(x, null)) return false;
                if (ReferenceEquals(y, null)) return false;
                if (x.GetType() != y.GetType()) return false;
                return x.CardNumber == y.CardNumber && x.CVV == y.CVV && x.Expiration == y.Expiration && x.CardHolder == y.CardHolder;
            }

            public int GetHashCode(CreditCard obj)
            {
                return HashCode.Combine(obj.CardNumber, obj.CVV, obj.Expiration, obj.CardHolder);
            }
        }

        public static IEqualityComparer<CreditCard> CreditCardComparer { get; set; } = new CreditCardEqualityComparer();
    }
}
