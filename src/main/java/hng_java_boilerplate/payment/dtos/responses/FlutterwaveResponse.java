package hng_java_boilerplate.payment.dtos.responses;

import lombok.ToString;

import java.math.BigDecimal;

@ToString
public class FlutterwaveResponse {

    private String status;
    private String message;
    private Data data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    @ToString
    public static class Data {
        private String id;
        private String tx_ref;
        private String flw_ref;
        private String status;
        private BigDecimal amount;
        private String currency;
        private String charged_amount;
        private String app_fee;
        private String merchant_fee;
        private String processor_response;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTx_ref() {
            return tx_ref;
        }

        public void setTx_ref(String tx_ref) {
            this.tx_ref = tx_ref;
        }

        public String getFlw_ref() {
            return flw_ref;
        }

        public void setFlw_ref(String flw_ref) {
            this.flw_ref = flw_ref;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getCharged_amount() {
            return charged_amount;
        }

        public void setCharged_amount(String charged_amount) {
            this.charged_amount = charged_amount;
        }

        public String getApp_fee() {
            return app_fee;
        }

        public void setApp_fee(String app_fee) {
            this.app_fee = app_fee;
        }

        public String getMerchant_fee() {
            return merchant_fee;
        }

        public void setMerchant_fee(String merchant_fee) {
            this.merchant_fee = merchant_fee;
        }

        public String getProcessor_response() {
            return processor_response;
        }

        public void setProcessor_response(String processor_response) {
            this.processor_response = processor_response;
        }
    }

}
