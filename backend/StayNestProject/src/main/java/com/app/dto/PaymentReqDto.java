package com.app.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentReqDto {
    @Min(value = 1, message = "Booking ID must be greater than 0")
    private Long bookingId;

    @Min(value = 1, message = "Amount must be greater than 0")
    private double amount;

    @NotBlank(message = "Payment method ID is required")
    private String paymentMethodId;
}
