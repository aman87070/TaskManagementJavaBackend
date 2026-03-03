package org.amancoding.demo.repository;

import org.amancoding.demo.entity.TenantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface TenantRepository extends JpaRepository<TenantEntity, Long> {

    Optional<TenantEntity> findByTenantId(String tenantId);

    Optional<TenantEntity> findBySubdomain(String subdomain);

    boolean existsBySubdomain(String subdomain);
}