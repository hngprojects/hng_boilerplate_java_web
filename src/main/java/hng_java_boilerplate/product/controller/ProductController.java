package hng_java_boilerplate.product.controller;

import hng_java_boilerplate.product.dto.ProductSearchDTO;
import hng_java_boilerplate.product.entity.Product;
import hng_java_boilerplate.product.product_mapper.ProductMapper;
import hng_java_boilerplate.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
            @RequestParam(value = "maxPrice", required = false) Double maxPrice) {

        if (name != null) {
            name = name.replaceAll("^\"|\"$", "").trim();
        }

        List<Product> products = productService.productsSearch(name, category, minPrice, maxPrice);
        ProductSearchDTO productSearchDTO = new ProductSearchDTO();

        if(products.isEmpty()){
            productSearchDTO.setStatus_code(HttpStatus.NO_CONTENT.value());
            productSearchDTO.setProducts(ProductMapper.INSTANCE.toDTOList(products));
            productSearchDTO.setSuccess(true);
            return new ResponseEntity<>(productSearchDTO, HttpStatus.OK);
        }

        productSearchDTO.setStatus_code(HttpStatus.OK.value());
        productSearchDTO.setProducts(ProductMapper.INSTANCE.toDTOList(products));
        productSearchDTO.setSuccess(true);
        return new ResponseEntity<>(productSearchDTO, HttpStatus.OK);

    }
}
