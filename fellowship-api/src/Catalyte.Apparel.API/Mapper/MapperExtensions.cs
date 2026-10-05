using AutoMapper;
using Catalyte.Apparel.Data.Models;
using Catalyte.Apparel.DTOs.Purchases;
using System;
using System.Collections.Generic;
using System.Linq;

namespace Catalyte.Apparel.API.DTOMappings
{
    public static class MapperExtensions
    {

        public static IEnumerable<PurchaseResponseDTO> MapPurchasesToPurchaseResponseDTOs(this IMapper mapper, IEnumerable<Purchase> purchases)
        {
            return purchases
                .Select(x => mapper.MapPurchaseToPurchaseResponseDTO(x))
                .ToList();
        }

        /// <summary>
        /// Helper method to build a PurchaseResponseDTO from a Purchase Model.
        /// </summary>
        /// <param name="purchase">The purchase to be persisted.</param>
        /// <returns>A purchase DTO.</returns>
        public static PurchaseResponseDTO MapPurchaseToPurchaseResponseDTO(this IMapper mapper, Purchase purchase)
        {
            return new PurchaseResponseDTO()
            {
                Id = purchase.Id,
                OrderDate = purchase.OrderDate,
                SubTotal = purchase.SubTotal,
                Shipping = purchase.Shipping,
                Total = purchase.Total,
                LineItems = mapper.Map<List<LineItemDTO>>(purchase.LineItems),
                DeliveryAddress = mapper.Map<DeliveryAddressDTO>(purchase), 
                BillingAddress = mapper.Map<BillingAddressDTO>(purchase),
                CreditCard = mapper.Map<CreditCardDTO>(purchase)
            };
        }

        public static Purchase MapCreatePurchaseDTOToPurchase(this IMapper mapper, PurchaseRequestDTO purchaseDTO)
        {
            var purchase = new Purchase
            {
                OrderDate = DateTime.UtcNow,
            };
            purchase = mapper.Map(purchaseDTO.DeliveryAddress, purchase);
            purchase = mapper.Map(purchaseDTO.BillingAddress, purchase);
            purchase = mapper.Map(purchaseDTO.CreditCard, purchase);
            purchase.LineItems = mapper.Map(purchaseDTO.LineItems, purchase.LineItems);

            return purchase;
        }
    }
}
