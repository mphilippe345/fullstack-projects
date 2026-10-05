using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;

namespace Catalyte.Apparel.Data.Models
{
    /// <summary>
    /// This class is a representation of a PromoCode.
    /// </summary>
    public class PromoCode : BaseEntity
    {
        public string Role { get; set; }

        [Required(ErrorMessage = "Title field is required")]
        [StringLength(20, ErrorMessage = "Title must be less than 20 characters")] 
        public string title { get; set; }

        [Required(ErrorMessage = "Description field is required")]
        [StringLength(100, ErrorMessage = "Description must be less than 100 characters")]
        public string description { get; set; }

        [Required(ErrorMessage = "Type field is required")]
        [RegularExpression("\\$|%", ErrorMessage = "The type must be $ or %")]
        public string type { get; set; }


        [Required(ErrorMessage = "Rate field is required")]
        [RateValidation]
        public decimal rate { get; set; }

        public class RateValidation : ValidationAttribute
        {
            protected override ValidationResult IsValid(object value, ValidationContext validationContext)
            {
                var promo = (PromoCode)validationContext.ObjectInstance;
                if (!(promo.type == "%" && promo.rate > 100))
                    if (promo.type == "%" && promo.rate > 0)
                        return ValidationResult.Success;

                if (promo.type != "%" && promo.type != "$")

                    return ValidationResult.Success;

                if (promo.type == "$")
                    if (!(promo.type == "$" && promo.rate <= 0))
                        return ValidationResult.Success;

                var promoStr = value as string;
                return string.IsNullOrWhiteSpace(promoStr)
                    ? new ValidationResult("If type is % rate must be between 0.01-100, If type is $ rate must be more than 0.")
                    : ValidationResult.Success;
            }
        }

        public override string ToString()
        {
            return JsonConvert.SerializeObject(this);
        }

        public static IEqualityComparer<PromoCode> PromoCodeComparer { get; } = new PromoCodeEqualityComparer();

        private sealed class PromoCodeEqualityComparer : IEqualityComparer<PromoCode>
        {
            public bool Equals(PromoCode x, PromoCode y)
            {
                if (ReferenceEquals(x, y)) return true;
                if (ReferenceEquals(x, null)) return false;
                if (ReferenceEquals(y, null)) return false;
                if (x.GetType() != y.GetType()) return false;
                return x.title.ToUpper() == y.title.ToUpper() && x.description == y.description && x.type == y.type && x.rate == y.rate;
            }

            public int GetHashCode(PromoCode obj)
            {
                var hashCode = new HashCode();
                hashCode.Add(obj.title.ToUpper());
                hashCode.Add(obj.description);
                hashCode.Add(obj.type);
                hashCode.Add(obj.rate);
                return hashCode.ToHashCode();
            }
        }
    }
}
