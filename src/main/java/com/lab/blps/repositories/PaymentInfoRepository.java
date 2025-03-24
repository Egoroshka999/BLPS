package com.lab.blps.repositories;

import com.lab.blps.models.PaymentInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentInfoRepository extends JpaRepository<PaymentInfo, Long> {
    PaymentInfo getPaymentInfoByAccountNumber(String accountNumber);
}
