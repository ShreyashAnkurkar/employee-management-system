package com.nit.dto.response;

import java.time.LocalDate;

import com.nit.entity.Employee.EmployeeStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String position;
    private Double salary;
    private LocalDate joiningDate;
    private EmployeeStatus status;
    private String departmentName;
}