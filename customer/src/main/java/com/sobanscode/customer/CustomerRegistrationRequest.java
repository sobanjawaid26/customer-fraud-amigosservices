package com.sobanscode.customer;

public record CustomerRegistrationRequest(
        String firstName,
        String lastName,
        String email
) {
}
