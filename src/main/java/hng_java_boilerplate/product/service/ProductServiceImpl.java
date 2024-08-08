package hng_java_boilerplate.product.service;

import hng_java_boilerplate.product.dto.ErrorDTO;
import hng_java_boilerplate.product.dto.ProductDTO;
import hng_java_boilerplate.product.dto.ProductUpdateRequestDto;
import hng_java_boilerplate.product.dto.ProductUpdateResponseDto;
import hng_java_boilerplate.product.entity.Product;
import hng_java_boilerplate.product.errorhandler.ProductNotFoundException;
import hng_java_boilerplate.product.exceptions.ValidationError;
import hng_java_boilerplate.product.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

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


    public ProductUpdateResponseDto updateProductStatus(ProductUpdateRequestDto productUpdate){

        Product product = productRepository.findById(productUpdate.getProduct_id()).stream()
                .filter(foundProduct -> foundProduct.getId().equals(productUpdate.getProduct_id()))
                .findFirst()
                .orElseThrow(() -> new ProductNotFoundException("Product with Id: " +productUpdate.getProduct_id()+ " not found"));


        if (productUpdate.getProduct_id() != null){

            product.setId(productUpdate.getProduct_id());

        }

        if (productUpdate.getName() != null){

            product.setName(productUpdate.getName());
        }

        if (productUpdate.getCategory() != null){

            product.setCategory(productUpdate.getCategory());
        }

        if (productUpdate.getPrice() != null ){

            product.setPrice(productUpdate.getPrice());
        }

        if ( productUpdate.getDescription() != null ){

            product.setDescription(productUpdate.getDescription());
        }

        if (productUpdate.getCurrent_stock() != null){

            product.setCurrentStock(productUpdate.getCurrent_stock());
        }

        if (productUpdate.getImage_Url() != null ){

            product.setImageUrl(productUpdate.getImage_Url());

        }

        if (productUpdate.getAvailable() != null){

            product.setIsAvailable(productUpdate.getAvailable());
        }

        productRepository.save(product);


        ProductDTO productDTO = new ProductDTO();

        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setDescription(product.getDescription());
        productDTO.setCategory(product.getCategory());
        productDTO.setPrice(product.getPrice());
        productDTO.setCurrent_stock(product.getCurrentStock());
        productDTO.setImageUrl(product.getImageUrl());
        productDTO.setStatus(product.getIsAvailable());


        ProductUpdateResponseDto productUpdateResponse = new ProductUpdateResponseDto();

        productUpdateResponse.setMessage("Success");
        productUpdateResponse.setStatus_code(HttpStatus.CREATED.value());
        productUpdateResponse.setData(productDTO);

        return productUpdateResponse;

    }


}
