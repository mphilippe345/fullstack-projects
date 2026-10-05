using System;

namespace Catalyte.Apparel.DTOs.Reviews
{
    /// <summary>
    /// Describes a data transfer object for a PromoCode.
    /// </summary>

    public class ReviewDTO
    {
        public string Email { get; set; }

        public int Id { get; set; }

        public decimal Rating { get; set; }

        public string Comment { get; set; }

        public string Name { get; set; }

        public DateTime DateCreated { get; set; }

        public DateTime DateModified { get; set; }
    }
}