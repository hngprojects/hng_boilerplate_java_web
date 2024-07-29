package hng_java_boilerplate.product.controller;

import hng_java_boilerplate.product.dto.*;
import hng_java_boilerplate.product.entity.Product;
import hng_java_boilerplate.product.product_mapper.ProductMapper;
import hng_java_boilerplate.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/search")
    public ResponseEntity<ProductSearchDTO> searchProducts(
            @RequestParam(value = "name") String name,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "minPrice", required = false) Double minPrice,
            @RequestParam(value = "maxPrice", required = false) Double maxPrice,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "limit", defaultValue = "10") int limit) {

        if (name != null) {
            name = name.replaceAll("^\"|\"$", "").trim();
        }

        Page<Product> products = productService.productsSearch(name, category, minPrice, maxPrice, PageRequest.of(page, limit));
        ProductSearchDTO productSearchDTO = new ProductSearchDTO();

        if(products.isEmpty()){
            productSearchDTO.setStatus_code(HttpStatus.NO_CONTENT.value());
            productSearchDTO.setProducts(ProductMapper.INSTANCE.toDTOList(products).getContent());
            productSearchDTO.setSuccess(true);
            productSearchDTO.setTotal(products.getTotalPages());
            productSearchDTO.setLimit(products.getSize());
            productSearchDTO.setPage(products.getNumber());
            return new ResponseEntity<>(productSearchDTO, HttpStatus.OK);
        }

        productSearchDTO.setStatus_code(HttpStatus.OK.value());
        productSearchDTO.setProducts(ProductMapper.INSTANCE.toDTOList(products).getContent());
        productSearchDTO.setTotal(products.getTotalPages());
        productSearchDTO.setLimit(products.getSize());
        productSearchDTO.setPage(products.getNumber());
        productSearchDTO.setSuccess(true);
        return new ResponseEntity<>(productSearchDTO, HttpStatus.OK);
    }


    @GetMapping("/{productId}/stock")
    public ResponseEntity<ProductStockDTO> getProductStock(@PathVariable String productId) {
        Product product = productService.getProductStock(productId);
        ProductStockDTO productStockDTO = new ProductStockDTO(
                product.getId(),
                product.getCurrentStock(),
                product.getLastUpdated()
        );
        return new ResponseEntity<>(productStockDTO, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> createProduct(@Valid @RequestBody ProductCreateDto productCreateDto) {
        // get the user from security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        System.out.println("This is the user name " +currentUsername);

        Product product = productService.createProduct(productCreateDto, currentUsername);

        ProductResponseDto response = ProductResponseDto.builder()
                .status("success")
                .message("Product created successfully")
                .data(ProductResponseInfo.builder()
                        .id(product.getId())
                        .name(product.getName())
                        .description(product.getDescription())
                        .category(product.getCategory())
                        .price(product.getPrice())
                        .createdAt(product.getCreatedAt())
                        .updatedAt(product.getUpdatedAt())
                        .build())
                .build();


        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
