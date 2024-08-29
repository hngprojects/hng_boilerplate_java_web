package hng_java_boilerplate.dashboard.controller;

import hng_java_boilerplate.dashboard.service.DashboardService;
import hng_java_boilerplate.product.dto.GetProductsDTO;
import hng_java_boilerplate.product.dto.ProductCountDto;
import hng_java_boilerplate.product.dto.ProductDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('SUPER_ADMIN')")
@RequestMapping("/api/v1/dashboards")
@Tag(name = "dashboard", description = "admin dashboard")
public class DashboardController {
    private final DashboardService dashboardService;

    @GetMapping("/products")
    @Operation(summary = "get all the products in the database")
    public ResponseEntity<GetProductsDTO> getProducts() {
        return ResponseEntity.ok(dashboardService.getAllProducts());
    }

    @GetMapping("/products/count")
    @Operation(summary = "get the total counts of products")
    public ResponseEntity<ProductCountDto> getProductCount() {
        return ResponseEntity.ok(dashboardService.getProductCount());
    }

    @GetMapping("/products/{productId}")
    @Operation(summary = "get a product by the product id")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable String productId) {
        return ResponseEntity.ok(dashboardService.getProductById(productId));
    }
}
