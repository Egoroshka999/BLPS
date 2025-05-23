package com.lab.blps.repositories.applications;

import com.lab.blps.models.applications.Application;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    Page<Application> findByDeveloperId(Long developerId, Pageable pageable);

    Application getApplicationById(Long id);

    @Query("SELECT a FROM Application a WHERE a.monetizationStatus = 'NONE' AND a.createdAt < :cutoff")
    List<Application> findByMonetizationNotRequestedAndCreatedAtBefore(@Param("cutoff") LocalDateTime cutoff);

}
