package org.amancoding.demo.dto;

import lombok.Data;

@Data
public class EmployeeDto {
    private Long id;
    private String name;
    private String phone;
    private String email;
}