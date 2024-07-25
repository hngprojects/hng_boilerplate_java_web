package hng_java_boilerplate.sale.service;


import hng_java_boilerplate.sale.dto.ResponseDTO;
import org.springframework.http.ResponseEntity;

public interface SaleService{
    ResponseEntity<ResponseDTO> getSummary();
    ResponseEntity<ResponseDTO> getPieChartData();
    ResponseEntity<ResponseDTO> getBarChartData();
    ResponseEntity<ResponseDTO> getLineChartData();
}
