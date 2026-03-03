package org.amancoding.demo.service.Impl;

import java.util.ArrayList;
import java.util.List;

import org.amancoding.demo.dto.EmployeeDto; // Correct Import
import org.amancoding.demo.entity.EmployeeEntity;
import org.amancoding.demo.repository.EmployeeRepository;
import org.amancoding.demo.service.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public String createEmployee(EmployeeDto employeeDto) {
        EmployeeEntity employeeEntity = new EmployeeEntity();
        BeanUtils.copyProperties(employeeDto, employeeEntity);
        employeeRepository.save(employeeEntity);
        return "Saved Successfully";
    }

    @Override
    public EmployeeDto readEmployee(Long id) {
        EmployeeEntity employeeEntity = employeeRepository.findById(id).get();
        EmployeeDto employeeDto = new EmployeeDto();
        BeanUtils.copyProperties(employeeEntity, employeeDto);
        return employeeDto;
    }

    @Override
    public List<EmployeeDto> readEmployees() {
        List<EmployeeEntity> employeesList = employeeRepository.findAll();
        List<EmployeeDto> employeesDtoList = new ArrayList<>();

        for (EmployeeEntity employeeEntity : employeesList) {
            EmployeeDto dto = new EmployeeDto();
            BeanUtils.copyProperties(employeeEntity, dto);
            employeesDtoList.add(dto);
        }
        return employeesDtoList;
    }

    @Override
    public boolean deleteEmployee(Long id) {
        // It's safer to delete directly by ID
        if (employeeRepository.existsById(id)) {
            employeeRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public String updateEmployee(Long id, EmployeeDto employeeDto) {
        // 1. Fix: Use findById, not findAll
        EmployeeEntity existingEmployee = employeeRepository.findById(id).get();

        // 2. Update fields
        existingEmployee.setEmail(employeeDto.getEmail());
        existingEmployee.setName(employeeDto.getName());
        existingEmployee.setPhone(employeeDto.getPhone());

        // 3. Fix: Save the ENTITY, not the repository
        employeeRepository.save(existingEmployee);

        return "Update Successfully";
    }
}