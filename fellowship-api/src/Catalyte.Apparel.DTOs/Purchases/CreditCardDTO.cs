namespace Catalyte.Apparel.DTOs.Purchases
{
    /// <summary>
    /// Describes a data transfer object for a credit card.
    /// </summary>
    public class CreditCardDTO
    {
        public string CardNumber { get; set; }

        public string CVV { get; set; }

        public string Expiration { get; set; }

        public string CardHolder { get; set; }
    }
}
