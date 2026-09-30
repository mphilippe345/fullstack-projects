package com.superhero.superheroapi.domains.product;

import com.superhero.superheroapi.exception.ServerError;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataAccessException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.superhero.superheroapi.domains.product.ProductHelper.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    ModelMapper mapper = new ModelMapper();

    Product testProduct;
    Product testProduct2;
    Product testProduct3;
    Product testProduct4;
    Product testProduct5;
    Product testProduct6;

    ProductStatusesModel productStatusesModel;
    List<ProductModel> productModels = new ArrayList<>();


    @BeforeEach
    public void setUp() {
        testProduct = generateValidProductWithGenre1();
        testProduct2 = generateValidProductWithGenre1();
        testProduct3 = generateValidProductWithGenre1();
        testProduct4 = generateValidProductWithGenre1();
        testProduct5 = generateValidProductWithGenre1();
        testProduct6 = generateValidProductWithGenre2();
        productModels.addAll(Arrays.asList(mapper.map(testProduct, ProductModel.class),
                mapper.map(testProduct2, ProductModel.class),
                mapper.map(testProduct3, ProductModel.class),
                mapper.map(testProduct4, ProductModel.class),
                mapper.map(testProduct5, ProductModel.class)));
        when(productRepository.findById(VALID_ID)).thenReturn(Optional.ofNullable(testProduct));
        productStatusesModel = generateValidProductStatusesModel();
    }

    @Test
    public void getRelatedProductsReturnsAList() {
        when(productRepository.getRelatedProducts(testProduct.getId())).thenReturn(List.of(testProduct, testProduct2, testProduct3, testProduct4, testProduct5));
        List<ProductModel> expected = productModels;
        List<ProductModel> actual = productService.getRelatedProducts(VALID_ID);
        assertEquals(expected, actual, "Test expects the actual and expected list of products to be the same.");
    }

    @Test
    public void getRelatedProductsReturnsAListWithTheCorrectSize() {
        when(productRepository.getRelatedProducts(testProduct.getId())).thenReturn(List.of(testProduct, testProduct2, testProduct3, testProduct4, testProduct5));
        int expected = 5;
        int actual = productService.getRelatedProducts(VALID_ID).size();
        assertEquals(expected, actual, "Test expects size of the list returned to be 5.");
    }

    @Test
    public void getRelatedProductsDoesNotIncludeProductWithDifferentGenre() {
        when(productRepository.getRelatedProducts(testProduct.getId())).thenReturn(List.of(testProduct, testProduct2, testProduct3, testProduct4, testProduct5));
        ProductModel mappedProduct = mapper.map(testProduct6, ProductModel.class);
        assertFalse(productService.getRelatedProducts(VALID_ID).contains(mappedProduct), "List of products returned should not include products with different genres.");
    }

    @Test
    public void getRelatedProductsThrowsDataAccessException() {
        when(productRepository.getRelatedProducts(anyLong())).thenThrow(new DataAccessException("Simulated exception") {});
        ServerError serverError = assertThrows(ServerError.class, () -> productService.getRelatedProducts(VALID_ID));
        assertEquals("Simulated exception", serverError.getMessage(), "The message in the error thrown do not match the one that's expected.");
    }

    @Test
    public void getProductStatusesReturnsCorrectActive() {
        boolean expectActive = true;
        boolean actualActive = productService.getProductStatusesById(VALID_ID).isActive();
        assertEquals(expectActive, actualActive, "The expected boolean values should be the same.");
    }

    @Test
    public void getProductStatusesReturnsCorrectStockStatus() {
        boolean expectStockStatus = true;
        boolean actualStockStatus = productService.getProductStatusesById(VALID_ID).isStockStatus();
        assertEquals(expectStockStatus, actualStockStatus, "The expected boolean values should be the same.");
    }
}