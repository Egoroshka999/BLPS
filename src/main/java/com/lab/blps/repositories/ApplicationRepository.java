package com.lab.blps.repositories;

import com.lab.blps.models.Application;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    Page<Application> findByDeveloperId(Long developerId, Pageable pageable);
}
