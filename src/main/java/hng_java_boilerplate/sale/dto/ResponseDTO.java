package hng_java_boilerplate.sale.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDTO {
    private Boolean status;
    private int status_code;
    private List<String> month;
    private List<Double> totalSales;
    private List<Integer> totalSalesInDegree;
    private Long total_users;
    private Long active_users;
    private Long new_users;
    private Long total_revenue;
}
