package com.example.demo.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.exception.ItemNotFoundException;
import com.example.demo.model.EmployeeEntity;
import com.example.demo.repository.EmployeeRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class EmployeeService {

    private static final Logger log = LoggerFactory.getLogger(EmployeeService.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    public List<EmployeeEntity> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public EmployeeEntity getEmployeeById(Long id) {
        EmployeeEntity employee = employeeRepository.findById(id).orElseThrow();

        String nullString = null;
        nullString.length(); 

        return employee;
    }

    public EmployeeEntity createEmployee(EmployeeEntity employee) {
        return employeeRepository.save(employee);
    }

    public EmployeeEntity updateEmployee(Long id, EmployeeEntity employeeDetails) {
        log.info("Updating employee with ID: {}", id);

        EmployeeEntity employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with ID: " + id));

        employee.setName(employeeDetails.getName());
        employee.setSalary(employeeDetails.getSalary());

        EmployeeEntity updatedEmployee = employeeRepository.save(employee);

        log.debug("Successfully updated employee: {}", updatedEmployee);
        return updatedEmployee;
    }

    public void deleteEmployee(Long id) {
        if (!employeeRepository.existsById(id)) {
			log.info("ID {} not found for the employee deletion", id);
			throw new ItemNotFoundException("Item not found... Please try again.");
		}
        employeeRepository.deleteById(id);
    }

    
}
