// Path: src/main/java/org/amancoding/demo/service/TenantService.java

package org.amancoding.demo.service;

import org.amancoding.demo.entity.TenantEntity;
import java.util.List;

public interface TenantService {
    // Sirf method ke naam likhein, logic nahi
    List<TenantEntity> getAllTenants();

    TenantEntity updateTenant(String tenantId, String companyName, String subdomain);
}