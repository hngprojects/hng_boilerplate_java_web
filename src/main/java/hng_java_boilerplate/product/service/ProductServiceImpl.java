package hng_java_boilerplate.product.service;

import hng_java_boilerplate.product.dto.ErrorDTO;
import hng_java_boilerplate.product.entity.Product;
import hng_java_boilerplate.product.exceptions.ValidationError;
import hng_java_boilerplate.product.repository.ProductRepository;
import hng_java_boilerplate.user.enums.Role;
import hng_java_boilerplate.product.exceptions.ProductNotFoundException;
import hng_java_boilerplate.product.exceptions.UnauthorizedAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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
    public void deleteProductById(String id) throws ProductNotFoundException, UnauthorizedAccessException {
        Product product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Product not found"));
        if (!hasPermissionToDelete(product)){
            throw new UnauthorizedAccessException("You do not have permission to delete this product");
        }
        productRepository.delete(product);
    }
    private boolean hasPermissionToDelete(Product product){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()){
            for (GrantedAuthority authority : authentication.getAuthorities()){
                Role role = Role.valueOf(authority.getAuthority().replace("ROLE_",""));
                if (role == Role.ROLE_ADMIN){
                    return true;
                }
            }
        }
        return false;
    }
}
