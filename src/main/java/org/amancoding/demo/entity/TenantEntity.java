package org.amancoding.demo.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "tenants")
@Data
public class TenantEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String tenantId; // UUID String

    @Column(unique = true, nullable = false)
    private String subdomain;

    private String companyName;
    private LocalDateTime createdAt = LocalDateTime.now();
}