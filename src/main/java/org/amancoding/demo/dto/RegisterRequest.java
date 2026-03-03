package org.amancoding.demo.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String companyName;
    private String subdomain;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String password;
}
