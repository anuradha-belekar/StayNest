package com.payment.service;

import com.payment.dto.ApiResponse;
import com.payment.dto.PaymentReqDto;
import com.stripe.exception.StripeException;

import jakarta.validation.Valid;

public interface PaymentService {

	ApiResponse createPayment(@Valid PaymentReqDto dto) throws StripeException;

}
