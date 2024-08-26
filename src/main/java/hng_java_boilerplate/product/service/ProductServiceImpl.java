package hng_java_boilerplate.product.service;
import hng_java_boilerplate.exception.NotFoundException;
import hng_java_boilerplate.product.dto.GetProductsDTO;
import hng_java_boilerplate.product.dto.ProductCountDto;
import hng_java_boilerplate.product.dto.ProductDTO;
import hng_java_boilerplate.product.entity.Product;
import hng_java_boilerplate.product.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

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

    private ProductDTO mapToProductDto(Product product) {
        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .description(product.getDescription())
                .category(product.getCategory())
                .image_url(product.getImageUrl())
                .build();
    }
}
