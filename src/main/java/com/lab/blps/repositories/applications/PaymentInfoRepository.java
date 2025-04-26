package com.lab.blps.repositories.applications;

import com.lab.blps.models.applications.PaymentInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentInfoRepository extends JpaRepository<PaymentInfo, Long> {
    PaymentInfo getPaymentInfoByAccountNumber(String accountNumber);
}
