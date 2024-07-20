package hng_java_boilerplate.product.product_mapper;

import hng_java_boilerplate.product.dto.ProductDTO;
import hng_java_boilerplate.product.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "category", target = "category")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "imageUrl", target = "imageUrl")
    ProductDTO toDTO(Product product);

    List<ProductDTO> toDTOList(List<Product> products);
}
