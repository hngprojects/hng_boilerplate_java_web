package hng_java_boilerplate.product.service;
import hng_java_boilerplate.exception.NotFoundException;
import hng_java_boilerplate.organisation.entity.Organisation;
import hng_java_boilerplate.organisation.repository.OrganisationRepository;
import hng_java_boilerplate.product.dto.GetProductsDTO;
import hng_java_boilerplate.product.dto.ProductCountDto;
import hng_java_boilerplate.product.dto.ProductDTO;
import hng_java_boilerplate.product.entity.Product;
import hng_java_boilerplate.product.repository.ProductRepository;
import hng_java_boilerplate.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final OrganisationRepository organisationRepository;

    @Override
    public Page<Product> productsSearch(String name, String category, Double minPrice, Double maxPrice, Pageable pageable) {
        return productRepository.searchProducts(name, category, minPrice, maxPrice, pageable);
    }

    @Override
    public ProductCountDto getProductsCount() {
        List<Product> products = productRepository.findAll();
        int productsCount = products.size();

        ProductCountDto.CountData data = new ProductCountDto
                .CountData(productsCount);

        return ProductCountDto
                .builder()
                .status_code(200)
                .status("success")
                .data(data)
                .build();
    }

    @Override
    public GetProductsDTO getProducts() {
        List<Product> products = productRepository.findAll();

        List<ProductDTO> productDTOS = products.stream()
                .map(this::mapToProductDto)
                .toList();

        return GetProductsDTO.builder()
                .status_code(200)
                .status("success")
                .data(productDTOS)
                .build();
    }

    @Override
    public ProductDTO getProductById(String productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("product not found with id: " + productId));
        return mapToProductDto(product);
    }

    @Override
    public ProductDTO createProduct(User user, Organisation organisation, ProductDTO productDTO) {
        Product product = productRepository.save(mapToProduct(productDTO, organisation, user));
        return mapToProductDto(product);
    }

    @Transactional
    @Override
    public void deleteProduct(Product product) {
        productRepository.delete(product);
    }

    @Override
    public ProductDTO editProduct(Product product, ProductDTO productDTO) {

        product.setName(productDTO.getName() != null ? productDTO.getName() : product.getName());
        product.setDescription(productDTO.getDescription() != null ? productDTO.getDescription() : product.getDescription());
        product.setPrice(productDTO.getPrice() != null ? productDTO.getPrice() : product.getPrice());
        product.setCategory(productDTO.getCategory() != null ? productDTO.getCategory() : product.getCategory());
        product.setImageUrl(productDTO.getImage_url() != null ? productDTO.getImage_url() : product.getImageUrl());
        return mapToProductDto(productRepository.save(product));
    }

    private ProductDTO mapToProductDto(Product product) {
        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .description(product.getDescription())
                .category(product.getCategory())
                .image_url(product.getImageUrl())
                .created_at(product.getCreatedAt())
                .updated_at(product.getUpdatedAt())
                .build();
    }

    public Product mapToProduct(ProductDTO product, Organisation organisation, User user) {
        return Product.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .description(product.getDescription())
                .category(product.getCategory())
                .imageUrl(product.getImage_url())
                .organisation(organisation)
                .user(user)
                .build();
    }
}
