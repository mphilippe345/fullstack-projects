package com.superhero.superheroapi.domains.inventory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    List<Inventory> findAllByProductId(Long id);

    Optional<Inventory> findByProductIdAndCondition(Long productId, String condition);

}
