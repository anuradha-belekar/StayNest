package com.app.client;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.app.dto.ApiResponse;
import com.app.dto.PaymentReqDto;

@Component
public class PaymentClient {
	
	
	private final RestTemplate restTemplate;
	
	@Value("${payment.service.url}")
	private String paymentServiceUrl;

	public PaymentClient(RestTemplate restTemplate) {
		super();
		this.restTemplate = restTemplate;
	}
	

	public ApiResponse makePayment(PaymentReqDto dto,String jwt) {
		HttpHeaders headers=new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		System.out.println("JWT passed to payment client: " + jwt);
		headers.setBearerAuth(jwt);
		HttpEntity<PaymentReqDto> request=new HttpEntity<>(dto,headers);
		
		ResponseEntity<ApiResponse> response=restTemplate.postForEntity(
				 "http://localhost:9091/payments", request, ApiResponse.class);
		
		return response.getBody();
	}
}
