package com.lab.blps.repositories.contracts;

import com.lab.blps.models.contracts.Contract;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContractRepository extends JpaRepository<Contract, Long> {
}
