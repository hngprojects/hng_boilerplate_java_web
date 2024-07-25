package hng_java_boilerplate.sale.repository;

import hng_java_boilerplate.sale.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleRepository extends JpaRepository<Sale, Long> {

}
