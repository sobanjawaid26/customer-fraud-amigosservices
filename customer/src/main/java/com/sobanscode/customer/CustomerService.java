package com.sobanscode.customer;

import com.sobanscode.clients.fraud.FraudCheckResponse;
import com.sobanscode.clients.fraud.FraudClient;
import com.sobanscode.clients.notification.NotificationClient;
import com.sobanscode.clients.notification.NotificationRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public record CustomerService(
        CustomerRepository customerRepository,
        RestTemplate restTemplate,
        FraudClient fraudClient,
        NotificationClient notificationClient) {
    public void registerCustomer(CustomerRegistrationRequest request) {
        Customer customer = Customer.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .build(); // BUILDER PATTERN
        // todo: check if email is valid
        // todo: check if email not taken
        customerRepository.saveAndFlush(customer);

        /*FraudCheckResponse fraudCheckResponse = restTemplate.getForObject(
          "http://localhost:8081/api/v1/fraud-check/{customerId}",
                FraudCheckResponse.class,
                customer.getId()
        );
        */
        FraudCheckResponse fraudCheckResponse = fraudClient.isFraudster(customer.getId());

        notificationClient.sendNotification(
                new NotificationRequest(
                        customer.getId(),
                        customer.getEmail(),
                        String.format("Hi %s, welcome to Amigoscode...",
                                customer.getFirstName())
                )
        );

        log.info(fraudCheckResponse.isFraudster().toString());
        if(fraudCheckResponse.isFraudster()){
            throw new IllegalStateException("fraudster");
        }

        // todo: send notification
    }
}
