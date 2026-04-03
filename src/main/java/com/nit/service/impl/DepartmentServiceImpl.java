package com.nit.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.nit.dto.request.DepartmentRequest;
import com.nit.dto.response.DepartmentResponse;
import com.nit.entity.Department;
import com.nit.exception.ResourceNotFoundException;
import com.nit.repository.DepartmentRepository;
import com.nit.service.DepartmentService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    @Override
    public DepartmentResponse createDepartment(DepartmentRequest request) {
        if (departmentRepository.existsByName(request.getName())) {
            throw new RuntimeException("Department already exists with name: " + request.getName());
        }
        Department department = Department.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();
        Department saved = departmentRepository.save(department);
        return mapToResponse(saved);
    }

    @Override
    public DepartmentResponse getDepartmentById(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));
        return mapToResponse(department);
    }

    @Override
    public List<DepartmentResponse> getAllDepartments() {
        return departmentRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public DepartmentResponse updateDepartment(Long id, DepartmentRequest request) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));
        department.setName(request.getName());
        department.setDescription(request.getDescription());
        Department updated = departmentRepository.save(department);
        return mapToResponse(updated);
    }

    @Override
    public void deleteDepartment(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));
        departmentRepository.delete(department);
    }

    // Helper method — converts Entity to Response DTO
    private DepartmentResponse mapToResponse(Department department) {
        return DepartmentResponse.builder()
                .id(department.getId())
                .name(department.getName())
                .description(department.getDescription())
                .totalEmployees(department.getEmployees() == null ? 0 : department.getEmployees().size())
                .build();
    }
}