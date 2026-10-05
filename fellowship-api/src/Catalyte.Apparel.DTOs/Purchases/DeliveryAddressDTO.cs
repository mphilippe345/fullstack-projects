namespace Catalyte.Apparel.DTOs.Purchases
{
    /// <summary>
    /// Describes a data transfer object for a purchase transaction delivery address.
    /// </summary>
    public class DeliveryAddressDTO
    {
        public string DeliveryFirstName { get; set; }

        public string DeliveryLastName { get; set; }

        public string DeliveryStreet { get; set; }

        public string DeliveryStreet2 { get; set; }

        public string DeliveryCity { get; set; }

        public string DeliveryState { get; set; }

        public string DeliveryZip { get; set; }
    }
}
