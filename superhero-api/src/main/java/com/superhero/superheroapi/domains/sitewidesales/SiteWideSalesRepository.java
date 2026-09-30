package com.superhero.superheroapi.domains.sitewidesales;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SiteWideSalesRepository extends JpaRepository<SiteWideSale, Long> {

  List<SiteWideSale> findAll();
}
