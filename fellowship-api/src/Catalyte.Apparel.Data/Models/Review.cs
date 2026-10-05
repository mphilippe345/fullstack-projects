using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;

namespace Catalyte.Apparel.Data.Models
{
    /// <summary>
    /// Describes the object for the billing address of the purchase.
    /// </summary>
    public class Review : BaseEntity
    {
        public string Email { get; set; }

        public decimal? Rating { get; set; }

        public string Comment { get; set; }

        public string Name { get; set; }

        public int? ProductId { get; set; }
    }
}
