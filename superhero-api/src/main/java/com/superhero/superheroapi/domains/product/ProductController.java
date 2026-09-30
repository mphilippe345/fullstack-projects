package com.superhero.superheroapi.domains.product;

import com.superhero.superheroapi.domains.inventory.InventoryModel;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static com.superhero.superheroapi.constants.Paths.BASE;
import static com.superhero.superheroapi.constants.Paths.PRODUCT_ENDPOINT;

@RestController
@RequestMapping(BASE + PRODUCT_ENDPOINT)
@RequiredArgsConstructor
public class ProductController {

    private final Logger logger = LoggerFactory.getLogger(ProductController.class);

    private final IProductService productService;

    ModelMapper mapper = new ModelMapper();

    @GetMapping
    public ResponseEntity<List<ProductModel>> getAllProducts() {
        logger.info("Returning all products...");
        return new ResponseEntity<>(productService.getAllProducts(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductModel> getProductById(@PathVariable Long id) {
        logger.info("Returning products with ID: " + id + "...");
        return new ResponseEntity<>(mapper.map(productService.getProductById(id), ProductModel.class),
                HttpStatus.OK);
    }

    @GetMapping("/{id}/statuses")
    public ResponseEntity<ProductStatusesModel> getOosAndActiveById(@PathVariable Long id) {
        logger.info("Returning products with ID: " + id + "...");
        return new ResponseEntity<>(productService.getProductStatusesById(id), HttpStatus.OK);
    }

    @GetMapping("/newrelease")
    public ResponseEntity<List<Product>> getProductsByRelease() {
        logger.info("Returning products that are new releases.");
        return new ResponseEntity<>(productService.getNewReleases(), HttpStatus.OK);
    }

    @GetMapping("/genre/{genre}")
    public ResponseEntity<List<Product>> getProductsByGenres(@PathVariable String genre) {
        logger.info("Returning products by genre:" + genre + "...");
        return new ResponseEntity<>(productService.getByGenre(genre), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ProductModel> createProduct(@RequestBody ProductModel product) {
        logger.info("Saving new product...");
        return new ResponseEntity<>(productService.createProduct(product), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductModel> updateProduct(@RequestBody ProductModel product,
                                                      @PathVariable Long id) {
        logger.info("Updating product with ID: " + id + "...");
        return new ResponseEntity<>(productService.updateProduct(product, id), HttpStatus.OK);
    }

    @PutMapping("/active/{id}")
    public ResponseEntity<ProductModel> setActiveStatus(@PathVariable Long id,
                                                        @RequestParam boolean active) {
        logger.info("Updating active status of product with ID: " + id + " to " + active + "...");
        return new ResponseEntity<>(productService.updateActiveStatus(id, active), HttpStatus.OK);
    }

    @GetMapping("/titles-and-images")
    public ResponseEntity<List<ProductTitleAndImageModel>> getAllTitlesAndImages() {
        return new ResponseEntity<>(productService.getProductTitlesAndImages(), HttpStatus.OK);
    }

    @GetMapping("/best-sellers")
    public ResponseEntity<List<ProductModel>> getTop10() {
        return new ResponseEntity<>(productService.getTop10Sellers(), HttpStatus.OK);
    }

    @GetMapping("/filter")
    public ResponseEntity<Page<ProductModel>> filter(@RequestParam(required = false) LocalDate releaseDate,
                                                     @RequestParam(required = false) List<String> authors,
                                                     @RequestParam(required = false) List<String> publishers,
                                                     @RequestParam(required = false) BigDecimal minPrice,
                                                     @RequestParam(required = false) BigDecimal maxPrice,
                                                     @RequestParam(required = false) Boolean newrelease,
                                                     @RequestParam(required = false) Boolean bestSeller,
                                                     @RequestParam(required = false) List<String> genres,
                                                     @RequestParam(required = false) Boolean active,
                                                     @RequestParam int page,
                                                     @RequestParam int size,
                                                     @RequestParam String sort,
                                                     @RequestParam(required = false) String search) {
        return new ResponseEntity<>(productService.filterProducts(releaseDate, authors, publishers, minPrice, maxPrice, newrelease, bestSeller, genres, active, page, size, sort, search), HttpStatus.OK);
    }

    @GetMapping("/filter-fields")
    public ResponseEntity<FilterFields> getFilterFields() {
        return new ResponseEntity<>(productService.getFilterFields(), HttpStatus.OK);
    }

    @GetMapping("/inventory/{productId}")
    public ResponseEntity<List<InventoryModel>> getAvailableInventory(@PathVariable Long productId) {
        logger.info("Request received for getAvailableInventory");
        return new ResponseEntity<>(productService.getAvailableInventory(productId), HttpStatus.OK);
    }

    @GetMapping("/related/{id}")
    public ResponseEntity<List<ProductModel>> getRelatedProductsById(@PathVariable Long id) {
        logger.info("Returning related products of product with ID: " + id + "...");
        return new ResponseEntity<>(productService.getRelatedProducts(id), HttpStatus.OK);
    }

}
