package hng_java_boilerplate.product.controller;

import hng_java_boilerplate.exception.NotFoundException;
import hng_java_boilerplate.exception.UnAuthorizedException;
import hng_java_boilerplate.organisation.entity.Organisation;
import hng_java_boilerplate.product.dto.ProductDTO;
import hng_java_boilerplate.product.dto.ProductSearchDTO;
import hng_java_boilerplate.product.entity.Product;
import hng_java_boilerplate.product.product_mapper.ProductMapper;
import hng_java_boilerplate.product.service.ProductService;
import hng_java_boilerplate.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name="Product")
public class ProductController {

    private final ProductService productService;

    @GetMapping("/products/search")
    @Operation(summary = "search for product by the name and other filters")
    public ResponseEntity<ProductSearchDTO> searchProducts(
            @NotEmpty(message = "name cannot be empty")
            @NotBlank(message = "name cannot be blank")
            @NotNull(message = "name cannot be null")
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

    @GetMapping("/products/{productId}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable String productId) {
        return ResponseEntity.ok(productService.getProductById(productId));
    }

    @PostMapping("/organisation/{org_id}/products")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "create a product by the product id")
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDTO,
                                                    @PathVariable("org_id") String orgId, Authentication authentication){

        User user = (User) authentication.getPrincipal();
        Organisation org = user.getOrganisations().stream()
                .filter(n -> n.getId().equals(orgId))
                .findFirst()
                .orElseThrow(() -> new UnAuthorizedException("Not Authorized"));

        return new ResponseEntity<>(productService.createProduct(user, org, productDTO), HttpStatus.CREATED);
    }

    @DeleteMapping("/products/{product_id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a product by the product id")
    public ResponseEntity<?> deleteProduct(@PathVariable("product_id") String productId,
                                           Authentication authentication){
        User user = (User) authentication.getPrincipal();
        Product product = user.getOrganisations().stream()
                .flatMap(organisation -> organisation.getProducts().stream())
                .filter(p -> p.getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Product not found"));

        productService.deleteProduct(product);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/products")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Edit a product")
    public ResponseEntity<ProductDTO> editProduct(@RequestBody ProductDTO productDTO, Authentication authentication){
        User user = (User) authentication.getPrincipal();
        Product product = user.getOrganisations().stream()
                .flatMap(organisation -> organisation.getProducts().stream())
                .filter(p -> p.getId().equals(productDTO.getName()))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Product not found"));
        return new ResponseEntity<ProductDTO>(productService.editProduct(product, productDTO), HttpStatus.OK);
    }
}
