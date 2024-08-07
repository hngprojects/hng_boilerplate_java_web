package hng_java_boilerplate.product.service;

import hng_java_boilerplate.product.dto.ErrorDTO;
import hng_java_boilerplate.product.dto.ProductDTO;
import hng_java_boilerplate.product.dto.ProductStatusRequestDto;
import hng_java_boilerplate.product.dto.ProductStatusResponseDto;
import hng_java_boilerplate.product.entity.Product;
import hng_java_boilerplate.product.errorhandler.ProductNotFoundException;
import hng_java_boilerplate.product.exceptions.ValidationError;
import hng_java_boilerplate.product.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service

public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;

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


    public ProductStatusResponseDto updateProductStatus(ProductStatusRequestDto productStatusRequestDto){

        ProductStatusResponseDto productStatusResponseDto = new ProductStatusResponseDto();

        Optional<Product> productSearch = productRepository.findById(productStatusRequestDto.getProductId());

        if (productSearch.isEmpty()) {

            throw  new ProductNotFoundException("Product With id: " +productStatusRequestDto.getProductId()+ " Not Found");
        }

        else {
            Product product = productSearch.get();
            product.setName(productStatusRequestDto.getName());
            product.setDescription(productStatusRequestDto.getDescription());
            product.setCategory(productStatusRequestDto.getCategory());
            product.setPrice(productStatusRequestDto.getPrice());
            product.setImageUrl(productStatusRequestDto.getImageUrl());
            product.setIsAvailable(productStatusRequestDto.getAvailabilityStatus());

            productRepository.save(product);

            ProductDTO productDTO = new ProductDTO();
            productDTO.setId(product.getId());
            productDTO.setName(product.getName());
            productDTO.setDescription(product.getDescription());
            productDTO.setCategory(product.getCategory());
            productDTO.setPrice(product.getPrice());
            productDTO.setImageUrl(product.getImageUrl());
            productDTO.setAvailabilityStatus(product.getIsAvailable());


            productStatusResponseDto.setMessage("Status updated successfully");
            productStatusResponseDto.setStatus_code(HttpStatus.CREATED.value());
            productStatusResponseDto.setProductDTO(productDTO);

            return  productStatusResponseDto;

        }

    }
}
