namespace Catalyte.Apparel.DTOs.PromoCode
{
    /// <summary>
    /// Describes a data transfer object for a PromoCode.
    /// </summary>

    public class PromoCodeDTO
    {
        public int id { get; set; }

        public string title { get; set; }

        public string description { get; set; }

        public string type { get; set; }

        public decimal rate { get; set; }
    }
}
