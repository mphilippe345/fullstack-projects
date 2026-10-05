using System;
using System.ComponentModel.DataAnnotations;

namespace Catalyte.Apparel.Data.Models
{
    /// <summary>
    /// This class represents a base for all other entities.
    /// </summary>
    public class BaseEntity
    {

        [Key]
        public int Id { get; set; }

        public DateTime DateCreated { get; set; }

        public DateTime DateModified { get; set; }

    }

}
