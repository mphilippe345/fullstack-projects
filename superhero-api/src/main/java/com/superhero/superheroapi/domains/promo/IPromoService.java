package com.superhero.superheroapi.domains.promo;

import java.util.List;

public interface IPromoService {

  List<PromoCode> getAllPromos();

  PromoCode createPromo(PromoCode promo);

  DiscountedComics assignPromo(Long promoId, Long comicId);

  List<PromoCode> findAssignedPromosByProductId(Long comicId);
}
