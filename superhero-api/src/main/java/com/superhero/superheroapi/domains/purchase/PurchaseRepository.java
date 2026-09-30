package com.superhero.superheroapi.domains.purchase;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

  @Query(value = "SELECT * FROM public.purchase WHERE email = ?1", nativeQuery = true)
  Page<Purchase> findByEmail(String userEmail, Pageable pageable);

}
