package com.superhero.superheroapi.domains.inventory;

import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final IInventoryService inventoryService;

    @GetMapping()
    public ResponseEntity<List<Inventory>> getAllInventory() {
        return new ResponseEntity<>(inventoryService.getAllInventory(), HttpStatus.OK);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<List<Inventory>> getInventoryByProductId(@PathVariable Long productId) {
        return new ResponseEntity<>(inventoryService.getInventoryByProductId(productId), HttpStatus.OK);
    }

}
