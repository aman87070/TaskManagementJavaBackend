package org.amancoding.demo.config;

import org.amancoding.demo.entity.TenantEntity;
import org.amancoding.demo.entity.UserEntity;
import org.amancoding.demo.repository.TenantRepository;
import org.amancoding.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    @Autowired
    private TenantRepository tenantRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        String adminEmail = "admin@gmail.com";
        String adminSubdomain = "admin"; // ✨ Super Admin ka default subdomain
        if (!tenantRepository.existsBySubdomain(adminSubdomain)) {
            TenantEntity systemTenant = new TenantEntity();
            systemTenant.setCompanyName("System Admin");
            systemTenant.setSubdomain(adminSubdomain);
            systemTenant.setTenantId("SYSTEM-001");
            systemTenant.setCreatedAt(java.time.LocalDateTime.now());
            tenantRepository.save(systemTenant);
            System.out.println("✅ System Tenant 'admin' create ho gaya.");
        }
        // 1. Check karein email aur subdomain dono ke base par
        if (!userRepository.existsByEmailAndSubdomain(adminEmail, adminSubdomain)) {

            UserEntity admin = new UserEntity();
            admin.setFirstName("Super");
            admin.setLastName("Admin");
            admin.setEmail("admin@gmail.com");
            admin.setUsername("superadmin");
            admin.setSubdomain("admin"); // ✨ Super admin ke liye 'admin' subdomain
            admin.setTenantId("SYSTEM-001"); // Default ID
            admin.setRole("ROLE_SUPER_ADMIN");
            admin.setPassword(passwordEncoder.encode("Admin@123"));

            userRepository.save(admin);
            System.out.println("✅ Super Admin create ho gaya: " + adminEmail + " (Subdomain: " + adminSubdomain + ")");
        } else {
            System.out.println("ℹ️ Super Admin pehle se exist karta hai.");
        }
    }
}