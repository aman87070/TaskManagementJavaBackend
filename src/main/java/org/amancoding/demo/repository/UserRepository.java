package org.amancoding.demo.repository;

import org.amancoding.demo.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List; // ✨ Ise import karna zaroori h
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    // ✨ 1. Ye naya method h jo error theek karega
    // Isse hum us tenant ke saare users ko dhoondh payenge
    List<UserEntity> findAllByTenantId(String tenantId);

    // ✨ 2. Multi-tenancy ke liye: email aur subdomain dono check karega
    boolean existsByEmailAndSubdomain(String email, String subdomain);

    boolean existsByEmail(String email);

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByUsername(String username);
}