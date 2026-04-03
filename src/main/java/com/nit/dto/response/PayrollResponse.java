package com.nit.dto.response;

import java.time.LocalDate;

import com.nit.entity.Payroll.PayrollStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PayrollResponse {
    private Long id;
    private Long employeeId;
    private String employeeName;
    private Double basicSalary;
    private Double hra;
    private Double allowances;
    private Double deductions;
    private Double netSalary;
    private LocalDate payDate;
    private Integer payMonth;
    private Integer payYear;
    private PayrollStatus status;
}