package org.amancoding.demo.service;

import java.util.List;
import org.amancoding.demo.dto.EmployeeDto;

public interface EmployeeService {
    String createEmployee(EmployeeDto employeeDto);

    List<EmployeeDto> readEmployees();

    boolean deleteEmployee(Long id);

    String updateEmployee(Long id, EmployeeDto employeeDto);

    EmployeeDto readEmployee(Long id);
}