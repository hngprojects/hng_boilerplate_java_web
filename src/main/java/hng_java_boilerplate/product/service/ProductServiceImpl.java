package hng_java_boilerplate.product.service;

import hng_java_boilerplate.product.dto.ErrorDTO;
import hng_java_boilerplate.product.dto.ProductDTO;
import hng_java_boilerplate.product.entity.Product;
import hng_java_boilerplate.product.exceptions.AuthenticationFailedException;
import hng_java_boilerplate.product.exceptions.RecordNotFoundException;
import hng_java_boilerplate.product.exceptions.ValidationError;
import hng_java_boilerplate.product.repository.ProductRepository;
import hng_java_boilerplate.user.entity.User;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.security.Principal;
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

    @Override
    public Product createProduct(ProductDTO productDTO, Principal principal) {
        var user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        Product product = new Product();
        BeanUtils.copyProperties(productDTO, product);
        product.setUser(user);
        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(ProductDTO productDTO, String productId, Principal principal) {
        var user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RecordNotFoundException("Not Found"));
        if(!product.getUser().getId().equals(user.getId())) throw new AuthenticationFailedException("Invalid Request");
        BeanUtils.copyProperties(productDTO, product);
        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(String productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RecordNotFoundException("Not found"));
        productRepository.delete(product);
    }

    @Override
    public Product getProductById(String productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new RecordNotFoundException("No Record Found"));
    }
}
