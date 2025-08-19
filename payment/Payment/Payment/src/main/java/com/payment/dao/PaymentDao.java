package com.payment.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.payment.entities.Payment;

public interface PaymentDao extends JpaRepository<Payment, Long>{

}
