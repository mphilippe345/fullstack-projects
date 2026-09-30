package com.superhero.superheroapi.domains.purchase;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IPurchaseService {

  List<Purchase> findAllPurchases();

  Page<PurchaseDto> getPurchasesByEmail(String email, int page, int size);

  PurchaseDto savePurchase(Purchase purchaseToSave);

}
