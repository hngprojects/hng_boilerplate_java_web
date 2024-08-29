package hng_java_boilerplate.product.service;

import hng_java_boilerplate.organisation.entity.Organisation;
import hng_java_boilerplate.product.dto.GetProductsDTO;
import hng_java_boilerplate.product.dto.ProductCountDto;
import hng_java_boilerplate.product.dto.ProductDTO;
import hng_java_boilerplate.product.entity.Product;
import hng_java_boilerplate.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {

    Page<Product> productsSearch(String name, String category, Double minPrice, Double maxPrice, Pageable pageable);
    ProductCountDto getProductsCount();
    GetProductsDTO getProducts();
    ProductDTO getProductById(String productId);
    ProductDTO createProduct(User user, Organisation orgId, ProductDTO productDTO);
    void deleteProduct(Product product);
    ProductDTO editProduct(Product product, ProductDTO productDTO);
}