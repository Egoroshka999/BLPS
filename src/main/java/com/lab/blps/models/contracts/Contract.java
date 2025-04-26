package com.lab.blps.models.contracts;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "contracts")
public class Contract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String pdfPath;

    @Enumerated(EnumType.STRING)
    private ContractStatus status;

    private Long applicationId;
}
