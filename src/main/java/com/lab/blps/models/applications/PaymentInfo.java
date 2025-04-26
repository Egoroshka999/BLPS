package com.lab.blps.models.applications;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "payment_info")
public class PaymentInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accountNumber;

    private PaymentStatus paymentStatus;  // признак корректности реквизитов

    @ManyToOne
    @JoinColumn(name = "developer_id")
    private User developer;
}

