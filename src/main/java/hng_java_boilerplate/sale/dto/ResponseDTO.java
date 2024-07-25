package hng_java_boilerplate.sale.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.flywaydb.core.api.ErrorDetails;

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
    private String error;
    private String message;
    private ErrorDetails details;


    public ResponseDTO(Boolean status, int status_code, String error, String message, ErrorDetails details){
        this.status = status;
        this.status_code = status_code;
        this.error = error;
        this.message = message;
        this.details = details;
    }
}
