package hng_java_boilerplate.payment.utils;

import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.CustomerSearchResult;
import com.stripe.net.RequestOptions;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.CustomerSearchParams;

import java.util.UUID;

public class CustomerUtils {
    public static Customer findOrCreateCustomer(String email, String name) throws StripeException {
        CustomerSearchParams params =
                CustomerSearchParams
                        .builder()
                        .setQuery("email:'" + email + "'")
                        .build();

        CustomerSearchResult result = Customer.search(params);
        Customer customer;

        if (result.getData().isEmpty()) {
            CustomerCreateParams customerCreateParams = CustomerCreateParams.builder()
                    .setName(name)
                    .setEmail(email)
                    .build();
            RequestOptions options = new RequestOptions.RequestOptionsBuilder().setIdempotencyKey(UUID.randomUUID().toString()).build();
            customer = Customer.create(customerCreateParams, options);
        } else {
            customer = result.getData().get(0);
        }
        return customer;
    }
}
