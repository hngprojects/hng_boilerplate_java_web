package hng_java_boilerplate.payment.dtos.reqests;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class PaymentRequest {

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private Integer amount;

    @NotEmpty(message = "Order ID is required")
    private String orderId;



    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }



}
