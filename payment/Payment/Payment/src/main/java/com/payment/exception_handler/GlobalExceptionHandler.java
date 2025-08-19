package com.payment.exception_handler;

import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.payment.custom_exceptions.PaymentProcessingException;
import com.payment.custom_exceptions.ResourceNotFoundException;
import com.payment.dto.ApiResponse;
import com.stripe.exception.StripeException;



@RestControllerAdvice
public class GlobalExceptionHandler {
	
	//to handle ResourceNOtFoundException
		@ExceptionHandler(ResourceNotFoundException.class)
		public ResponseEntity<?> handleResourceNOtFoundException(ResourceNotFoundException e){
			System.out.println("in catch- Resource not found Exception");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage()));//sc404
		}

		
		
		 @ExceptionHandler(PaymentProcessingException.class)
		    public ResponseEntity<ApiResponse> handlePaymentProcessingException(PaymentProcessingException e) {
		        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(e.getMessage()));
		    }
		 
		 @ExceptionHandler(StripeException.class)
		    public ResponseEntity<ApiResponse> handleStripeException(StripeException e) {
		        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("Payment failed: " + e.getMessage()));
		    }
		 
		@ExceptionHandler(RuntimeException.class)
	    public ResponseEntity<ApiResponse> handleRuntimeException(RuntimeException e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage()));
	    }
		
		
}
