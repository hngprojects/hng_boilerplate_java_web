package hng_java_boilerplate.product.service;

import hng_java_boilerplate.product.dto.GetProductsDTO;
import hng_java_boilerplate.product.dto.ProductCountDto;
import hng_java_boilerplate.product.dto.ProductDTO;
import hng_java_boilerplate.product.entity.Product;
import hng_java_boilerplate.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private ProductServiceImpl underTest;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId("prod-id");
        product.setName("product");
        product.setDescription("prod-desc");
        product.setCategory("prod-cat");
    }


    @Test
    void shouldGetProductsCount() {
        when(productRepository.findAll()).thenReturn(List.of(product));

        ProductCountDto response = underTest.getProductsCount();

        assertThat(response.status_code()).isEqualTo(200);
        assertThat(response.status()).isEqualTo("success");
        assertThat(response.data()).hasFieldOrPropertyWithValue("count", 1);

        verify(productRepository).findAll();
    }

    @Test
    void getProducts() {
        when(productRepository.findAll()).thenReturn(List.of(product));
        GetProductsDTO products = underTest.getProducts();

        assertThat(products.status_code()).isEqualTo(200);
        assertThat(products.status()).isEqualTo("success");
        assertThat(products.data()).hasSize(1);
        assertThat(products.data().get(0)).hasFieldOrPropertyWithValue("id", product.getId());
        assertThat(products.data().get(0)).hasFieldOrPropertyWithValue("name", product.getName());

        verify(productRepository).findAll();
    }

    @Test
    void getProductById() {
        when(productRepository.findById("prod-id")).thenReturn(Optional.of(product));

        ProductDTO response = underTest.getProductById("prod-id");

        assertThat(response.getId()).isEqualTo(product.getId());
        assertThat(response.getName()).isEqualTo(product.getName());
        assertThat(response.getDescription()).isEqualTo(product.getDescription());

        verify(productRepository).findById("prod-id");
    }
}