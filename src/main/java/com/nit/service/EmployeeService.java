package com.nit.service;

import com.nit.dto.request.EmployeeRequest;
import com.nit.dto.response.EmployeeResponse;
import java.util.List;

public interface EmployeeService {
    EmployeeResponse createEmployee(EmployeeRequest request);
    EmployeeResponse getEmployeeById(Long id);
    List<EmployeeResponse> getAllEmployees();
    List<EmployeeResponse> getEmployeesByDepartment(Long departmentId);
    EmployeeResponse updateEmployee(Long id, EmployeeRequest request);
    void deleteEmployee(Long id);
}