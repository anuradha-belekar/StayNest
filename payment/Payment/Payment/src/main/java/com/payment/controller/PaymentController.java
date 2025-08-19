package com.payment.controller;

import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.payment.dto.PaymentReqDto;
import com.payment.service.PaymentService;
import com.stripe.exception.StripeException;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/payments")
@AllArgsConstructor
public class PaymentController {
	
	private final PaymentService paymentService;
	
	@PostMapping
	@PreAuthorize("hasRole('CUSTOMER')")
	@Operation(description = "create a new payment")
	public ResponseEntity<?> createPayment(@RequestBody @Valid PaymentReqDto dto) throws StripeException{
		return ResponseEntity.status(HttpStatus.CREATED).body(paymentService.createPayment(dto));
	}

}
