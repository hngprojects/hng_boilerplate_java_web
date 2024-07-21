package hng_java_boilerplate.product.controller;

import hng_java_boilerplate.product.dto.ProductErrorResponse;
import hng_java_boilerplate.product.dto.ProductResponseDto;
import hng_java_boilerplate.product.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService){
        this.productService = productService;
    }

    @GetMapping("/{product_id}")
    public ResponseEntity<?> fetchProductById(@PathVariable("product_id") String productId){
        Optional<ProductResponseDto> product = productService.findProductById(productId);
        if(product.isPresent()){
            return ResponseEntity.ok(product);
        }else {
            ProductErrorResponse errorResponse = new ProductErrorResponse("Product not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    //    to fulfill all righteousness
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException exp){
        var errors = new HashMap<String, String>();
        exp.getBindingResult().getAllErrors().forEach(error -> {
            var fieldName = ((FieldError) error).getField();
            var errorMessage = error.getDefaultMessage();

            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
