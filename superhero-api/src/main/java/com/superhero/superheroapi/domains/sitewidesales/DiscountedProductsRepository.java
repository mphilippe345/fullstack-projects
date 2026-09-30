package com.superhero.superheroapi.domains.sitewidesales;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscountedProductsRepository extends JpaRepository<DiscountedProducts, Long> {

}
