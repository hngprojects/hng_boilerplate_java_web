package hng_java_boilerplate.product.repository;

import hng_java_boilerplate.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, String> {
}
