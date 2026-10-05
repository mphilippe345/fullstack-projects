using System;
using System.Collections.Generic;

namespace Catalyte.Apparel.DTOs.Purchases
{
    /// <summary>
    /// Describes a data transfer object for a purchase transaction.
    /// </summary>
    public class PurchaseResponseDTO
    {
        public int Id { get; set; }

        public decimal SubTotal { get; set; }

        public decimal Shipping { get; set; }

        public decimal Total { get; set; }

        public DateTime OrderDate { get; set; }

        public DeliveryAddressDTO DeliveryAddress { get; set; }

        public BillingAddressDTO BillingAddress { get; set; }

        public CreditCardDTO CreditCard { get; set; }

        public List<LineItemDTO> LineItems { get; set; }
    }
}
