package com.nit.service;

import com.nit.dto.request.DepartmentRequest;
import com.nit.dto.response.DepartmentResponse;
import java.util.List;

public interface DepartmentService {
    DepartmentResponse createDepartment(DepartmentRequest request);
    DepartmentResponse getDepartmentById(Long id);
    List<DepartmentResponse> getAllDepartments();
    DepartmentResponse updateDepartment(Long id, DepartmentRequest request);
    void deleteDepartment(Long id);
}