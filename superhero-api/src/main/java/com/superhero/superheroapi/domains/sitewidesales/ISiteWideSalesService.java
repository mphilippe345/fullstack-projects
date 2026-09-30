package com.superhero.superheroapi.domains.sitewidesales;

import java.util.List;

public interface ISiteWideSalesService {

  List<SiteWideSale> getAllSiteWideSales();

  SiteWideSale createSiteWideSale(SiteWideSale newSiteWideSale);

  SiteWideSale updateSiteWideSaleById(SiteWideSale newSiteWideSale, Long siteWideSaleId);

  SiteWideSale deleteSiteWideSaleById(Long siteWideSaleId);

  DiscountedProducts assignSiteWideSale(Long siteWideSaleId, Long productId);
}
