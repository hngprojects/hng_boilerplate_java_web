package hng_java_boilerplate.product.controller;

import hng_java_boilerplate.product.dto.ProductErrorDTO;
import hng_java_boilerplate.product.dto.ProductSearchDTO;
import hng_java_boilerplate.product.entity.Product;
import hng_java_boilerplate.product.exceptions.ProductNotFoundException;
import hng_java_boilerplate.product.exceptions.UnauthorizedAccessException;
import hng_java_boilerplate.product.product_mapper.ProductMapper;
import hng_java_boilerplate.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProductById(@PathVariable String id){
        try{
            productService.deleteProductById(id);
            return ResponseEntity.noContent().build();
        }catch (ProductNotFoundException e){
            ProductErrorDTO errorDTO = new ProductErrorDTO(false,"Product not found",HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
        }catch (UnauthorizedAccessException e){
            ProductErrorDTO errorDTO = new ProductErrorDTO(false,"You do not have permission to delete this product",HttpStatus.FORBIDDEN.value());
            return new ResponseEntity<>(errorDTO, HttpStatus.FORBIDDEN);
        }catch (Exception e){
            ProductErrorDTO errorDTO = new ProductErrorDTO(false,"Internal server error",HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
