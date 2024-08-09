package hng_java_boilerplate.product.service;

import hng_java_boilerplate.product.dto.ErrorDTO;
import hng_java_boilerplate.product.dto.ProductDTO;
import hng_java_boilerplate.product.dto.ProductInventoryDto;
import hng_java_boilerplate.product.dto.ProductUpdateResponseDto;
import hng_java_boilerplate.product.entity.Product;
import hng_java_boilerplate.product.errorhandler.ProductNotFoundException;
import hng_java_boilerplate.product.exceptions.ValidationError;
import hng_java_boilerplate.product.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.List;

@Service

public class ProductServiceImpl implements ProductService{

    private ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Page<Product> productsSearch(String name, String category, Double minPrice, Double maxPrice, Pageable pageable) {
        if (name == null || name.isEmpty()) {
            ErrorDTO errorDTO = new ErrorDTO();
            errorDTO.setMessage("name is a required parameter");
            errorDTO.setParameter("name");
            throw new ValidationError(errorDTO);
        }
        return productRepository.searchProducts(name, category, minPrice, maxPrice, pageable);
    }

    public ProductUpdateResponseDto availableProductStock(String productId) {
        Product product = productRepository.findById(productId).stream()
                .filter(foundProduct -> foundProduct.getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ProductNotFoundException("Product With id: " +productId+ " Not Found"));

        ProductInventoryDto productInventoryDto = new ProductInventoryDto();
        productInventoryDto.setProduct_Id(product.getId());
        productInventoryDto.setCurrent_Stock(product.getCurrentStock());
        productInventoryDto.setLastUpdated_at(product.getUpdatedAt());

        ProductUpdateResponseDto productStatusResponseDto =new ProductUpdateResponseDto();

        productStatusResponseDto.setMessage("success");
        productStatusResponseDto.setStatus_code(HttpStatus.OK.value());
        productStatusResponseDto.setData(productInventoryDto);

        return productStatusResponseDto;

    }

    public ProductUpdateResponseDto getProductById(String productId) {
        Product product = productRepository.findById(productId).stream()
                .filter(foundProduct -> foundProduct.getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ProductNotFoundException("Product With id: " +productId+ " Not Found"));

        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setDescription(product.getDescription());
        productDTO.setCategory(product.getCategory());
        productDTO.setPrice(product.getPrice());
        productDTO.setImageUrl(product.getImageUrl());
        productDTO.setStatus(product.getIsAvailable());
        productDTO.setCurrent_stock(product.getCurrentStock());

        ProductUpdateResponseDto productStatusResponseDto =new ProductUpdateResponseDto();

        productStatusResponseDto.setMessage("success");
        productStatusResponseDto.setStatus_code(HttpStatus.OK.value());
        productStatusResponseDto.setProductData(productDTO);

        return productStatusResponseDto;

    }

}
