package com.superhero.superheroapi.domains.product;

import com.superhero.superheroapi.domains.inventory.Inventory;
import com.superhero.superheroapi.domains.inventory.InventoryModel;
import com.superhero.superheroapi.domains.inventory.InventoryRepository;
import com.superhero.superheroapi.exception.NotFound;
import com.superhero.superheroapi.exception.ServerError;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.superhero.superheroapi.constants.StringConstants.*;

/**
 * Service implementation for the IProductService interface.
 *
 * @author Ricardo Reyes-Benavides
 */
@Service
@RequiredArgsConstructor
@EnableTransactionManagement
public class ProductService implements IProductService {

    private final Logger logger = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository productRepository;

    private final InventoryRepository inventoryRepository;

    private final ModelMapper mapper = new ModelMapper();

    private final List<String> conditions = Arrays.asList("new", "like-new", "very-good", "good", "acceptable");

    /**
     * Fetches all products from the database and coverts them to a product model.
     *
     * @return List of all the products in the database in the form of models.
     */
    @Override
    public List<ProductModel> getAllProducts() {
        try {
            List<Product> allProducts = productRepository.getAllByActive(true);
            List<ProductModel> mappedList = new ArrayList<>();
            allProducts.forEach(product -> {
                ProductModel mappedProduct = mapper.map(product, ProductModel.class);
                mappedList.add(mappedProduct);
            });
            return mappedList;
        } catch (DataAccessException e) {
            logger.error(e.getMessage());
            throw new ServerError(e.getMessage());
        }
    }

    /**
     * Creates a product and persists the data in the database.
     *
     * @param product Product that is being created
     * @return Product that was saved to the database.
     */
    @Override
    @Transactional
    public ProductModel createProduct(ProductModel product) {
        try {
            Product productToSave = mapper.map(product, Product.class);
            Product persistedProduct = productRepository.save(productToSave);

            //save into the inventory with one of each condition
            conditions.forEach(condition -> {
                Inventory inventory = new Inventory();
                switch (condition) {
                    case "like-new" -> inventory.setRate(new BigDecimal(LIKE_NEW_DISCOUNT));
                    case "very-good" -> inventory.setRate(new BigDecimal(VERY_GOOD_DISCOUNT));
                    case "good" -> inventory.setRate(new BigDecimal(GOOD_DISCOUNT));
                    case "acceptable" -> inventory.setRate(new BigDecimal(ACCEPTABLE_DISCOUNT));
                    default -> inventory.setRate(new BigDecimal("1"));
                }
                inventory.setProduct(persistedProduct);
                inventory.setAmount(0);
                inventory.setDateAdded(LocalDate.now());
                inventory.setCondition(condition);
                inventoryRepository.save(inventory);
            });

            return mapper.map(productToSave, ProductModel.class);
        } catch (DataAccessException e) {
            logger.error(e.getMessage());
            throw new ServerError(e.getMessage());
        }
    }

    /**
     * Fetches a single product by its ID.
     *
     * @param id ID of the product that is being requested.
     * @return Product that was requested.
     */
    @Override
    public Product getProductById(Long id) {
        try {
            Optional<Product> productOptional = productRepository.findById(id);
            if (productOptional.isEmpty()) {
                logger.error("Product with ID of: " + id + " was not found.");
                throw new NotFound("Product with ID of: " + id + " was not found.");
            }
            return productOptional.get();
        } catch (DataAccessException e) {
            logger.error(e.getMessage());
            throw new ServerError(e.getMessage());
        }
    }

    /**
     * Updates fields of a products and persists the data in the database.
     *
     * @param product Product fields that are being updated.
     * @param id      ID of product that is being updated.
     * @return Product that was updated.
     */
    @Override
    public ProductModel updateProduct(ProductModel product, Long id) {
        try {
            getProductById(id);
            String productSku = product.getSku();

//      sku validation for updating
//      Optional<Product> skuCheck = productRepository.findOthersBySku(productSku, id);
//      if (skuCheck.isPresent()) {
//        throw new Conflict(
//            "There is already a product in the database with the SKU: " + productSku);
//      }

            Product productToSave = mapper.map(product, Product.class);
            productToSave.setId(id);

            return mapper.map(productRepository.save(productToSave), ProductModel.class);
        } catch (DataAccessException e) {
            logger.error(e.getMessage());
            throw new ServerError(e.getMessage());
        }
    }

    /**
     * Sets the active status of a given product. Can be set to true or false. Setting to false is
     * performing a soft delete.
     *
     * @param id     ID of product to alter.
     * @param active active boolean that is being assigned to the product.
     */
    @Override
    public ProductModel updateActiveStatus(Long id, boolean active) {
        try {
            Product product = getProductById(id);

            product.setActive(active);

            return mapper.map(productRepository.save(product), ProductModel.class);
        } catch (DataAccessException e) {
            logger.error(e.getMessage());
            throw new ServerError(e.getMessage());
        }
    }

    /**
     * Gets a list of products that are considered to be new releases based on the boolean value.
     *
     * @return a list of products
     */
    @Override
    public List<Product> getNewReleases() {
   /* try {
      List<Product> allProducts = productRepository.findAllByNewRelease(newRelease);
      List<ProductModel> mappedList = new ArrayList<>();
      allProducts.forEach(product -> {
        ProductModel mappedProduct = mapper.map(product, ProductModel.class);
        mappedList.add(mappedProduct);
      });
      return mappedList;*/
        try {
            return productRepository.findAllByNewrelease(true);
        } catch (DataAccessException e) {
            logger.error(e.getMessage());
            throw new ServerError(e.getMessage());
        }
    }

    /**
     * get products based on the genre that is provided in the parameters.
     *
     * @param genre genre of products that is in the request
     * @return List of products filtered by the genre
     */
    @Override
    public List<Product> getByGenre(String genre) {
        try {
            return productRepository.findAllByGenre(genre);
        } catch (DataAccessException e) {
            logger.error(e.getMessage());
            throw new ServerError(e.getMessage());
        }
    }

    /**
     * all product's title and images use only for the search bar on the front end.
     *
     * @return ProductTitleAndImageModel used to transfer product data with less fields.
     */
    @Override
    public List<ProductTitleAndImageModel> getProductTitlesAndImages() {
        try {
            List<Product> allProducts = productRepository.getAllByActive(true);
            List<ProductTitleAndImageModel> mappedList = new ArrayList<>();
            allProducts.forEach(product -> {
                ProductTitleAndImageModel mappedProduct = mapper.map(product,
                        ProductTitleAndImageModel.class);
                mappedList.add(mappedProduct);
            });
            return mappedList;
        } catch (DataAccessException e) {
            logger.error(e.getMessage());
            throw new ServerError(e.getMessage());
        }
    }

    /**
     * gets the top ten best-selling products.
     *
     * @return top ten selling products.
     */
    @Override
    public List<ProductModel> getTop10Sellers() {
        try {
            List<Product> top10products = productRepository.getTop10Sellers();
            List<ProductModel> models = new ArrayList<>();

            top10products.forEach(product -> {
                ProductModel model = mapper.map(product, ProductModel.class);
                models.add(model);
            });

            return models;
        } catch (DataAccessException e) {
            logger.error(e.getMessage());
            throw new ServerError(e.getMessage());
        }
    }

    /**
     * Filter method to return products based on fields inputted.
     *
     * @param releaseDate date for filter, accepts products that came on or before the LocalDate
     *                    provided.
     * @param authors     List of String, accepts authors that are on the list.
     * @param publishers  List of String, accepts publishers that are on the list.
     * @param minPrice    BigDecimal price, accepts products that are at least this price.
     * @param maxPrice    BigDecimal price, accepts products that are less than this price.
     * @param newrelease  Boolean, whether a new release or not.
     * @param bestSeller  Boolean, whether a bestseller or not.
     * @param genres      List of String, accepts genres that are on the list.
     * @param active      Boolean, whether a product is active or not.
     * @param page        page number of the products requested. (zero-indexed)
     * @param size        size of the page you want, in other words, how many elements needed.
     * @param sort        String of field needed to sort it on the front-end
     * @param search      String of search term. Searches for similar title, author, or publisher.
     * @return Page of products that fit the criteria needed.
     */
    @Override
    public Page<ProductModel> filterProducts(LocalDate releaseDate, List<String> authors,
                                             List<String> publishers,
                                             BigDecimal minPrice, BigDecimal maxPrice, Boolean newrelease, Boolean bestSeller,
                                             List<String> genres, Boolean active, int page, int size, String sort, String search) {
        try {
            Specification<Product> productSpecification;

            productSpecification = Specification.where(
                            ProductSpecification.withReleaseDateLessThanOrEqual(releaseDate)
                                    .and(ProductSpecification.withAuthors(authors)) // marked for removal
                                    .and(ProductSpecification.withPublishers(publishers))
                                    .and(ProductSpecification.withPriceRange(minPrice, maxPrice))
                                    .and(ProductSpecification.withNewRelease(newrelease))
                                    .and(ProductSpecification.withBestSeller(bestSeller))
                                    .and(ProductSpecification.withGenres(genres))
                                    .and(ProductSpecification.withActive(active)))
                    .and(ProductSpecification.withSearch(search));

            PageRequest pr = sort(page, size, sort);
            Page<Product> filteredProducts = productRepository.findAll(productSpecification, pr);
            List<ProductModel> productModels = new ArrayList<>();
            filteredProducts.forEach(product -> {
                ProductModel productModel = mapper.map(product, ProductModel.class);
                productModels.add(productModel);
            });

            return getProductModelPages(pr, productModels, filteredProducts);
        } catch (DataAccessException e) {
            logger.error(e.getMessage());
            throw new ServerError(e.getMessage());
        }
    }

    /**
     * gets all the fields that the front-end filter will need.
     *
     * @return FilterFields object for the front-end to use.
     */
    @Override
    public FilterFields getFilterFields() {
        try {
            FilterFields fields = new FilterFields();
            fields.setPublishers(productRepository.getAllPublishers());
            fields.setGenres(productRepository.getAllGenres());
            fields.setAuthors(productRepository.getAllAuthors());
            return fields;
        } catch (DataAccessException e) {
            logger.error(e.getMessage());
            throw new ServerError(e.getMessage());
        }
    }

    /**
     * Maps product entities and product models, to then return a Page object of the needed products
     * according to the PageRequest provided. When mapping, maps the correct price according to
     * condition.
     *
     * @param pr            PageRequest that includes page, size, sort.
     * @param productModels Product models to be Paged
     * @param productPage   Product Page that needs to be returns.
     * @return product Page of requested products.
     */
    private Page<ProductModel> getProductModelPages(PageRequest pr, List<ProductModel> productModels,
                                                    Page<Product> productPage) {
        try {
            List<Long> idsOnPage = productPage.getContent().stream()
                    .map(Product::getId).toList();
            productModels.removeIf(productModel -> !idsOnPage.contains(productModel.getId()));
            productModels.sort((productA, productB) -> {
                Product mappedProductA = mapper.map(productA, Product.class);
                Product mappedProductB = mapper.map(productB, Product.class);
                return productPage.getContent().indexOf(mappedProductA) - productPage.getContent()
                        .indexOf(mappedProductB);
            });
            productModels.forEach(product -> {
                for (String condition : conditions) {
                    Optional<Inventory> inventory = inventoryRepository.findByProductIdAndCondition(
                            product.getId(), condition);
                    if (inventory.isPresent() && inventory.get().getAmount() > 0) {
                        product.setPrice(inventory.get().getConditionPrice().setScale(2, RoundingMode.HALF_UP));
                        break;
                    }
                }
            });
            return new PageImpl<>(productModels, pr, productPage.getTotalElements());
        } catch (DataAccessException e) {
            logger.error(e.getMessage());
            throw new ServerError(e.getMessage());
        }
    }

    /**
     * Gets inventory items for a product given the inventory count is not 0
     *
     * @param productId id of the product
     * @return list of inventory items for a product that are available
     */
    @Override
    public List<InventoryModel> getAvailableInventory(Long productId) {
        try {
            var inventoryForProduct = inventoryRepository.findAllByProductId(productId);
            var inventoryModels = new ArrayList<>(inventoryForProduct.stream()
                    .map(inventory -> mapper.map(inventory, InventoryModel.class)).toList());
            inventoryModels.removeIf(inventoryModel -> inventoryModel.getAmount() == 0);

            if (inventoryModels.isEmpty()) {
                String error = String.format("No items with product id: %s are active or in stock", productId);
                logger.error(error);
                throw new NotFound(error);
            }

            logger.info(String.format("Got inventory of product with id: %s", productId));
            return inventoryModels;
        } catch (DataAccessException dae) {
            logger.error(dae.getMessage());
            throw new ServerError(dae.getMessage());
        }
    }

    /**
     * Gets 5 random products from the database that match the genres of the product with the ID that was passed in.
     *
     * @param id ID of the product that the related products are being requested for.
     * @return List of 5 related products
     */
    @Override
    public List<ProductModel> getRelatedProducts(Long id) {
        getProductById(id);

        List<Product> relatedProducts;
        List<ProductModel> mappedProducts = new ArrayList<>();
        try {
            relatedProducts = productRepository.getRelatedProducts(id);
        } catch (DataAccessException e) {
            logger.error(e.getMessage());
            throw new ServerError(e.getMessage());
        }
        relatedProducts.forEach(product -> mappedProducts.add(mapper.map(product, ProductModel.class)));
        return mappedProducts;
    }

    /**
     * Gets the active and stock status of a product.
     * @param id product id of the product.
     * @return Model of a product with 2 fields, the stock status and active status.
     */
    public ProductStatusesModel getProductStatusesById(Long id) {
        Product product = getProductById(id);
        return new ProductStatusesModel(product.getActive(), product.getStockStatus());
    }

    /**
     * Builds a PageRequest based on the provided page, size, and sort.
     *
     * @param page page number of the products requested. (zero-indexed)
     * @param size size of the page you want, in other words, how many elements needed.
     * @param sort String of field needed to sort it on the front-end
     * @return PageRequest to be able to use in the repository query.
     */
    private PageRequest sort(int page, int size, String sort) {
        PageRequest pr;
        switch (sort) {
            case "priceLow" -> pr = PageRequest.of(page, size, Sort.by("price").ascending());
            case "priceHigh" -> pr = PageRequest.of(page, size, Sort.by("price").descending());
            case "newRelease" -> pr = PageRequest.of(page, size, Sort.by("newrelease").descending());
            case "title" -> pr = PageRequest.of(page, size, Sort.by(sort));
            default -> pr = PageRequest.of(page, size);
        }
        return pr;
    }

    /**
     * scheduled method that fires off monthly to update the bestsellers.
     */
    @Scheduled(cron = "0 0 0 1 * *")
    @Transactional
    public void updateBestSellersMonthly() {
        updateBestSellers();
    }

    /**
     * scheduled method that fires off once when the server is initiated, should contain any method
     * that needs to happen when the server initiates.
     */
    @Scheduled(fixedRate = Long.MAX_VALUE)
    @Transactional
    public void updateFieldsOnStartUp() {
        updateBestSellers();
        updateStockStatusAllProducts();
    }

    /**
     * method that updates the bestsellers in the database.
     */
    public void updateBestSellers() {
        try {
            logger.info("Updating best sellers...");
            List<Product> bestSellers = productRepository.getTop10Sellers();
            List<Product> allProducts = productRepository.findAll();
            allProducts.forEach(product -> {
                product.setBestSeller(bestSellers.contains(product));
                productRepository.save(product);
            });


        } catch (DataAccessException e) {
            logger.error(e.getMessage());
            throw new ServerError(e.getMessage());
        }
    }

    /**
     * method that updates the stocks status of a given product, based on the amount of inventory in
     * the inventory table for each product.
     *
     * @param product product that needs to be updated.
     */
    public void updateStockStatus(Product product) {
        try {
            List<Inventory> inventories = inventoryRepository.findAllByProductId(product.getId());
            product.setStockStatus(false);
            for (Inventory inventory : inventories) {
                if (inventory.getAmount() > 0) {
                    product.setStockStatus(true);
                    break;
                }
            }
            productRepository.save(product);
        } catch (DataAccessException e) {
            logger.error(e.getMessage());
            throw new ServerError(e.getMessage());
        }
    }

    /**
     * updates the stock status of all the products in the database, sets stock status based on the
     * amount of inventory in the database.
     */
    public void updateStockStatusAllProducts() {
        try {
            logger.info("updating stock status on products...");
            List<Product> allProducts = productRepository.getAllByActive(true);
            allProducts.forEach(this::updateStockStatus);
        } catch (DataAccessException e) {
            logger.error(e.getMessage());
            throw new ServerError(e.getMessage());
        }
    }

}



