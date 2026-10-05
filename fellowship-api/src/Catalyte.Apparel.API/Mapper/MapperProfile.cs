using AutoMapper;
using Catalyte.Apparel.Data.Models;
using Catalyte.Apparel.DTOs;
using Catalyte.Apparel.DTOs.PromoCode;
using Catalyte.Apparel.DTOs.Products;
using Catalyte.Apparel.DTOs.Purchases;
using Catalyte.Apparel.DTOs.Reviews;
using Catalyte.Apparel.DTOs.WishListItems;

namespace Catalyte.Apparel.API
{
    public class MapperProfile : Profile
    {
        public MapperProfile()
        {
            CreateMap<Product, ProductDTO>()
                .ForMember(dest => dest.Price, opt => opt.MapFrom(src => src.Price + 0.00m))
                .ReverseMap();
            //As it is iterating through the products, it will make sure the prices are to 2 decimal places.

            CreateMap<PurchaseRequestDTO, Purchase>();

            CreateMap<Purchase, PurchaseResponseDTO>();
            CreateMap<Purchase, DeliveryAddressDTO>().ReverseMap();
            CreateMap<Purchase, CreditCardDTO>().ReverseMap();
            CreateMap<Purchase, BillingAddressDTO>()
                .ForMember(dest => dest.Email, opt => opt.MapFrom(src => src.BillingEmail))
                .ForMember(dest => dest.Phone, opt => opt.MapFrom(src => src.BillingPhone))
                .ReverseMap();

            CreateMap<LineItem, LineItemDTO>().ReverseMap();

            CreateMap<User, UserDTO>().ReverseMap();

            CreateMap<PromoCode, PromoCodeDTO>().ReverseMap();

            CreateMap<Review, ReviewDTO>().ReverseMap();

            CreateMap<WishListItem, WishListItemDTO>().ReverseMap();
        }

    }
}
