package com.superhero.superheroapi.domains.product;

import com.superhero.superheroapi.domains.inventory.InventoryModel;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface IProductService {

    List<ProductModel> getAllProducts();

    ProductModel createProduct(ProductModel product);

    Product getProductById(Long id);

    ProductModel updateProduct(ProductModel product, Long id);

    ProductModel updateActiveStatus(Long id, boolean active);

    List<Product> getNewReleases();

    List<Product> getByGenre(String genre);

    List<ProductTitleAndImageModel> getProductTitlesAndImages();

    List<ProductModel> getTop10Sellers();

    Page<ProductModel> filterProducts(LocalDate releaseDate, List<String> authors, List<String> publishers, BigDecimal minPrice, BigDecimal maxPrice, Boolean newrelease, Boolean bestSeller, List<String> genres, Boolean active, int page, int size, String sort, String search);

    FilterFields getFilterFields();

    List<InventoryModel> getAvailableInventory(Long productId);

    List<ProductModel> getRelatedProducts(Long id);

    ProductStatusesModel getProductStatusesById(Long id);
}
