namespace Catalyte.Apparel.DTOs.Purchases
{
    /// <summary>
    /// Describes a data transfer object for a purchase transaction billing address.
    /// </summary>
    public class BillingAddressDTO
    {
        public string BillingStreet { get; set; }

        public string BillingStreet2 { get; set; }

        public string BillingCity { get; set; }

        public string BillingState { get; set; }

        public string BillingZip { get; set; }

        public string Email { get; set; }

        public string Phone { get; set; }
    }
}
