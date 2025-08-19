package com.payment.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.payment.dao.PaymentDao;
import com.payment.dto.ApiResponse;
import com.payment.dto.PaymentReqDto;
import com.payment.entities.Payment;
import com.payment.entities.PaymentStatus;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentDao paymentDao;

    @Value("${stripe.api.key}") // Inject your Stripe secret key from application.properties
    private String stripeApiKey;

    @Override
    public ApiResponse createPayment(PaymentReqDto dto) throws StripeException {
        // Set Stripe API key
        Stripe.apiKey = stripeApiKey;

        // Create a PaymentIntent (backend-only card payment flow)
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount((long) dto.getAmount() * 100) // amount in paisa
                .setCurrency("inr")
                .setPaymentMethod(dto.getPaymentMethodId()) // Required when confirming from backend
                .setConfirm(true) // Confirm immediately from backend
                .setAutomaticPaymentMethods(
                        PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                .setEnabled(true)
                                .setAllowRedirects(PaymentIntentCreateParams.AutomaticPaymentMethods.AllowRedirects.NEVER)
                                .build()
                )
                .build(); // ✅ FIXED: close the builder call

        PaymentIntent paymentIntent = PaymentIntent.create(params);

        // Save payment record in DB
        Payment payment = new Payment(
                dto.getBookingId(),
                paymentIntent.getId(),
                dto.getAmount(),
                "succeeded".equals(paymentIntent.getStatus()) ? PaymentStatus.SUCCESS : PaymentStatus.PENDING
        );

        paymentDao.save(payment);

        return new ApiResponse("Payment created successfully with ID: " + payment.getId());
    }
}
