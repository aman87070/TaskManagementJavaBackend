package org.amancoding.demo.service.Impl;

import lombok.RequiredArgsConstructor;
import org.amancoding.demo.dto.LoginRequest;
import org.amancoding.demo.dto.LoginResponse;
import org.amancoding.demo.dto.RegisterRequest;
import org.amancoding.demo.entity.TenantEntity;
import org.amancoding.demo.entity.UserEntity;
import org.amancoding.demo.repository.TenantRepository;
import org.amancoding.demo.repository.UserRepository;
import org.amancoding.demo.security.JwtService;
import org.amancoding.demo.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final TenantRepository tenantRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public String register(RegisterRequest request) {
        // 1. Check if Email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
        }

        // 2. ✨ Subdomain Handling (Automatic Slug Logic)
        String subdomain = request.getSubdomain();
        if (subdomain == null || subdomain.trim().isEmpty()) {
            // Agar frontend se subdomain nahi aaya, toh Company Name se bana lo
            subdomain = request.getCompanyName().replaceAll("\\s+", "").toLowerCase().replaceAll("[^a-z0-9]", "");
        } else {
            subdomain = subdomain.toLowerCase().trim().replaceAll("\\s+", "");
        }

        // 3. Reserved Subdomain Check
        if ("admin".equalsIgnoreCase(subdomain)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This subdomain is reserved for system use.");
        }

        // 4. Subdomain Uniqueness Check
        if (tenantRepository.existsBySubdomain(subdomain)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Subdomain already taken. Please try another one.");
        }

        // 5. Create New Tenant
        String newTenantId = UUID.randomUUID().toString();
        TenantEntity tenant = new TenantEntity();
        tenant.setTenantId(newTenantId);
        tenant.setCompanyName(request.getCompanyName());
        tenant.setSubdomain(subdomain);
        tenantRepository.save(tenant);

        // 6. Create Admin User for this Tenant
        UserEntity user = new UserEntity();
        user.setUsername(request.getUsername());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setTenantId(newTenantId); // Link to the new tenant
        user.setSubdomain(subdomain);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("ROLE_ADMIN");
        userRepository.save(user);

        return "Registration Successful! Your workspace is: " + subdomain + ".localhost:3000";
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        // 1. Verify if the Workspace (Tenant) exists
        TenantEntity tenant = tenantRepository.findBySubdomain(request.getSubdomain())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Workspace not found. Check your URL."));

        // 2. Authenticate User Credentials
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
        }

        // 3. Find User
        UserEntity user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        // 4. ✨ Tenant Isolation Check (Very Important!)
        // Pakka karo ki ye user usi workspace ka hai jahan se login kar raha hai
        if (!user.getTenantId().equals(tenant.getTenantId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "You do not have access to the " + tenant.getCompanyName() + " workspace.");
        }

        // 5. Generate Tokens
        String accessToken = jwtService.generateAccessToken(user.getEmail());
        String refreshToken = jwtService.generateRefreshToken(user.getEmail());

        // 6. Return response with Company Name for Frontend Dashboard
        return new LoginResponse(
                "Login Successfully",
                accessToken,
                refreshToken,
                user.getEmail(),
                user.getTenantId(),
                tenant.getCompanyName(),
                user.getRole()

        );
    }

}