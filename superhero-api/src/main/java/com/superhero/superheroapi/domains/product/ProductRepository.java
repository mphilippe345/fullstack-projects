package com.superhero.superheroapi.domains.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    List<Product> getAllByActive(boolean active);

    Page<Product> findAllByActive(boolean active, Pageable pr);

    Optional<Product> findBySku(String sku);

    @Query(value = "SELECT * FROM product WHERE sku= :sku AND id != :id", nativeQuery = true)
    Optional<Product> findOthersBySku(@Param("sku") String sku, @Param("id") Long id);

    List<Product> findAllByNewrelease(boolean newrelease);

    List<Product> findAllByGenre(String genre);

    @Query(value = "SELECT * FROM product p WHERE (p.title ILIKE %:searchTerm% OR p.author ILIKE %:searchTerm% or p.publisher ILIKE %:searchTerm%) AND p.active = true ", nativeQuery = true)
    Page<Product> search(String searchTerm, Pageable pageable);

    @Query(value = "SELECT p.* FROM product p " +
            "INNER JOIN (SELECT product_id FROM line_item WHERE date_time >= CURRENT_DATE - INTERVAL '3 MONTH' GROUP BY product_id ORDER BY SUM(quantity) DESC LIMIT 10) top_products ON p.id = top_products.product_id", nativeQuery = true)
    List<Product> getTop10Sellers();

    @Query(value = "SELECT DISTINCT author FROM product", nativeQuery = true)
    List<String> getAllAuthors();

    @Query(value = "SELECT DISTINCT publisher FROM product", nativeQuery = true)
    List<String> getAllPublishers();

    @Query(value = "SELECT DISTINCT genre FROM product", nativeQuery = true)
    List<String> getAllGenres();

    @Query(value = "SELECT * FROM product p WHERE p.genre = (SELECT genre FROM product WHERE id = :id) AND p.id <> :id AND p.active = true ORDER BY RANDOM() LIMIT 5", nativeQuery = true)
    List<Product> getRelatedProducts(Long id);
}
