package hng_java_boilerplate.dashboard.service;

import hng_java_boilerplate.product.dto.GetProductsDTO;
import hng_java_boilerplate.product.dto.ProductCountDto;
import hng_java_boilerplate.product.dto.ProductDTO;
import hng_java_boilerplate.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final ProductService productService;

    public ProductCountDto getProductCount() {
        return productService.getProductsCount();
    }

    public GetProductsDTO getAllProducts() {
        return productService.getProducts();
    }

    public ProductDTO getProductById(String productId) {
        return productService.getProductById(productId);
    }
}
