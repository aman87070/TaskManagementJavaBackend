package org.amancoding.demo.controller;

import org.amancoding.demo.entity.TenantEntity;
import org.amancoding.demo.service.TenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/super-admin")
@CrossOrigin("*")
public class SuperAdminController {

    @Autowired
    private TenantService tenantService;

    // --- 1. GET API: Fetch All Tenants ---
    @GetMapping("/tenants")
    public ResponseEntity<List<TenantEntity>> getAllTenants() {
        return ResponseEntity.ok(tenantService.getAllTenants());
    }

    // --- 2. UPDATE API: Update Company & Subdomain ---
    // ✨ ID ka type badal kar 'String' kiya kyunki frontend se UUID aa raha h
    @PutMapping("/tenants/{tenantId}")
    public ResponseEntity<?> updateTenant(
            @PathVariable String tenantId, // 👈 Long ki jagah String use karein
            @RequestBody TenantEntity updateData) { // Aap yahan DTO bhi use kar sakte hain
        try {
            // Service ko 'String' tenantId pass karein
            TenantEntity updated = tenantService.updateTenant(
                    tenantId,
                    updateData.getCompanyName(),
                    updateData.getSubdomain());

            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            // Agar tenant nahi mila ya koi error aaya
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}