using System;
using System.Collections.Generic;

namespace Catalyte.Apparel.Data.Models
{
    /// <summary>
    /// Describes the object for a WishListItem.
    /// </summary>
    public class WishListItem : BaseEntity
    {
        public string UserEmail { get; set; }

        public int UserID { get; set; }

        public int ProductID { get; set; }

        public string ProductName { get; set; }

        public Product Product { get; set; }

        public User User { get; set; }

    
    }
}