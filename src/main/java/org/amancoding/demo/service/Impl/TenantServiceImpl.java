package org.amancoding.demo.service.Impl;

import org.amancoding.demo.entity.TenantEntity;
import org.amancoding.demo.entity.UserEntity;
import org.amancoding.demo.repository.TenantRepository;
import org.amancoding.demo.repository.UserRepository;
import org.amancoding.demo.service.TenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class TenantServiceImpl implements TenantService {

    @Autowired
    private TenantRepository tenantRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<TenantEntity> getAllTenants() {
        return tenantRepository.findAll();
    }

    @Override
    @Transactional // ✨ Dono tables (Tenant & User) ko ek saath update karne ke liye
    public TenantEntity updateTenant(String tenantId, String companyName, String subdomain) {

        // 1. UUID String ke basis par database se tenant dhundiye
        TenantEntity tenant = tenantRepository.findByTenantId(tenantId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Tenant not found with ID: " + tenantId));

        // 2. Agar Subdomain badal raha h, toh uniqueness check karein
        if (subdomain != null && !subdomain.equals(tenant.getSubdomain())) {
            if (tenantRepository.existsBySubdomain(subdomain)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Subdomain '" + subdomain + "' already taken!");
            }

            // Purana subdomain users update karne ke liye
            tenant.setSubdomain(subdomain);

            // ✨ CRITICAL: Us tenant ke saare users ka subdomain bhi update karein
            List<UserEntity> users = userRepository.findAllByTenantId(tenantId);
            for (UserEntity user : users) {
                user.setSubdomain(subdomain);
            }
            userRepository.saveAll(users);
        }

        // 3. Company Name update karein
        if (companyName != null && !companyName.trim().isEmpty()) {
            tenant.setCompanyName(companyName);
        }

        // 4. Save changes and Return
        return tenantRepository.save(tenant);
    }
}