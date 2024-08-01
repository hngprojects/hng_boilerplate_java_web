package hng_java_boilerplate.product.controller;

import hng_java_boilerplate.product.dto.ProductDTO;
import hng_java_boilerplate.product.dto.ProductSearchDTO;
import hng_java_boilerplate.product.entity.Product;
import hng_java_boilerplate.product.exceptions.AuthenticationFailedException;
import hng_java_boilerplate.product.exceptions.RecordNotFoundException;
import hng_java_boilerplate.product.product_mapper.ProductMapper;
import hng_java_boilerplate.product.service.ProductService;
import hng_java_boilerplate.product.utils.GeneralResponseEntity;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@Tag(name="Product")
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

    @PostMapping("/")
    public ResponseEntity<GeneralResponseEntity<Product>> createProduct(@RequestBody ProductDTO productDTO,
                                                                     Principal principal) throws Exception {
        GeneralResponseEntity<Product> generalResponseEntity = new GeneralResponseEntity<>();

        Product createdProduct = productService.createProduct(productDTO, principal);
        generalResponseEntity.setMessage("Product created successfully");
        generalResponseEntity.setInfo(createdProduct);
        return ResponseEntity.ok(generalResponseEntity);

    }

    @PutMapping("/{productId}")
    public ResponseEntity<GeneralResponseEntity<Product>> updateProduct(@RequestBody ProductDTO productDTO,
                                                                  String productId, Principal principal) {

        GeneralResponseEntity<Product> generalResponseEntity = new GeneralResponseEntity<>();
        try {
            Product updatedProduct = productService.updateProduct(productDTO, productId, principal);
            if (ObjectUtils.isEmpty(updatedProduct)) throw new RecordNotFoundException("Not Found");

            generalResponseEntity.setMessage("Product updated successfully");
            generalResponseEntity.setInfo(updatedProduct);
            return ResponseEntity.ok(generalResponseEntity);

        } catch (RecordNotFoundException | AuthenticationFailedException ex) {
            throw ex;

        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/{productId}")
    public ResponseEntity<GeneralResponseEntity<Product>> getProduct(@PathVariable String productId) {
        Product product = new Product();
        GeneralResponseEntity<Product> generalResponseEntity = new GeneralResponseEntity<>();
        try {
            Product savedProduct = productService.getProductById(productId);
            generalResponseEntity.setInfo(savedProduct);
            return ResponseEntity.ok(generalResponseEntity);
        } catch (RecordNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<GeneralResponseEntity<Product>> deleteProduct(@PathVariable String productId) {
        GeneralResponseEntity<Product> generalResponseEntity = new GeneralResponseEntity<>();
        try {
            productService.deleteProduct(productId);
            generalResponseEntity.setMessage("Product deleted successfully");
            return ResponseEntity.ok(generalResponseEntity);
        } catch (RecordNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

}
