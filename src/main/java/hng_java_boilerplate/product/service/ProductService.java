package hng_java_boilerplate.product.service;

import hng_java_boilerplate.product.dto.ProductDTO;
import hng_java_boilerplate.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.security.Principal;
import java.util.List;

public interface ProductService {

    //Method to Search for products with certain criteria, returns a list of products
    Page<Product> productsSearch(String name, String category, Double minPrice, Double maxPrice, Pageable pageable);

    // Add Product
    public Product createProduct(ProductDTO productDTO, Principal principal);

    // Update Product
    public Product updateProduct(ProductDTO productDTO, String productId, Principal principal);

    // Delete Product
    public void deleteProduct(String productId);

    // Get Product By Id
    public Product getProductById(String productId);
}