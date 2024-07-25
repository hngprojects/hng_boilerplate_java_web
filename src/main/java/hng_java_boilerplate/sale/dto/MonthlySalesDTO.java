package hng_java_boilerplate.sale.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonthlySalesDTO {
    private String month;
    private Double totalSales;
}
