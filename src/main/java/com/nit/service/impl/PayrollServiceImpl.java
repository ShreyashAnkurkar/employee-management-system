package com.nit.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.nit.dto.request.PayrollRequest;
import com.nit.dto.response.PayrollResponse;
import com.nit.entity.Employee;
import com.nit.entity.Payroll;
import com.nit.entity.Payroll.PayrollStatus;
import com.nit.exception.ResourceNotFoundException;
import com.nit.repository.EmployeeRepository;
import com.nit.repository.PayrollRepository;
import com.nit.service.EmailService;
import com.nit.service.PayrollService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PayrollServiceImpl implements PayrollService {

    private final PayrollRepository payrollRepository;
    private final EmployeeRepository employeeRepository;
    private final EmailService emailService;

    @Override
    public PayrollResponse generatePayroll(PayrollRequest request) {
        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + request.getEmployeeId()));

        if (payrollRepository.existsByEmployeeIdAndPayMonthAndPayYear(
                request.getEmployeeId(), request.getPayMonth(), request.getPayYear())) {
            throw new RuntimeException("Payroll already generated for this employee for the given month and year");
        }

        // Auto calculate salary components
        Double basicSalary = employee.getSalary();
        Double hra         = Math.round(basicSalary * 0.40 * 100.0) / 100.0;
        Double allowances  = Math.round(basicSalary * 0.20 * 100.0) / 100.0;
        Double deductions  = Math.round(basicSalary * 0.10 * 100.0) / 100.0;
        Double netSalary   = Math.round((basicSalary + hra + allowances - deductions) * 100.0) / 100.0;

        Payroll payroll = Payroll.builder()
                .employee(employee)
                .basicSalary(basicSalary)
                .hra(hra)
                .allowances(allowances)
                .deductions(deductions)
                .netSalary(netSalary)
                .payDate(LocalDate.now())
                .payMonth(request.getPayMonth())
                .payYear(request.getPayYear())
                .status(PayrollStatus.GENERATED)
                .build();

        Payroll saved = payrollRepository.save(payroll);
        return mapToResponse(saved);
    }

    @Override
    public PayrollResponse getPayrollById(Long id) {
        Payroll payroll = payrollRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payroll not found with id: " + id));
        return mapToResponse(payroll);
    }

    @Override
    public List<PayrollResponse> getAllPayrolls() {
        return payrollRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<PayrollResponse> getPayrollsByEmployee(Long employeeId) {
        return payrollRepository.findByEmployeeId(employeeId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<PayrollResponse> getPayrollsByMonthAndYear(Integer month, Integer year) {
        return payrollRepository.findByPayMonthAndPayYear(month, year)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PayrollResponse markAsPaid(Long id) {
        Payroll payroll = payrollRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payroll not found with id: " + id));
        payroll.setStatus(PayrollStatus.PAID);
        Payroll saved = payrollRepository.save(payroll);

        // Send payslip email
        try {
            emailService.sendPayslipEmail(
                payroll.getEmployee().getEmail(),
                payroll.getEmployee().getFirstName() + " " + payroll.getEmployee().getLastName(),
                payroll.getNetSalary(),
                payroll.getPayMonth(),
                payroll.getPayYear()
            );
        } catch (Exception e) {
            System.out.println("Email sending failed: " + e.getMessage());
        }

        return mapToResponse(saved);
    }

    @Override
    public void deletePayroll(Long id) {
        Payroll payroll = payrollRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payroll not found with id: " + id));
        payrollRepository.delete(payroll);
    }

    private PayrollResponse mapToResponse(Payroll payroll) {
        return PayrollResponse.builder()
                .id(payroll.getId())
                .employeeId(payroll.getEmployee().getId())
                .employeeName(payroll.getEmployee().getFirstName() + " " + payroll.getEmployee().getLastName())
                .basicSalary(payroll.getBasicSalary())
                .hra(payroll.getHra())
                .allowances(payroll.getAllowances())
                .deductions(payroll.getDeductions())
                .netSalary(payroll.getNetSalary())
                .payDate(payroll.getPayDate())
                .payMonth(payroll.getPayMonth())
                .payYear(payroll.getPayYear())
                .status(payroll.getStatus())
                .build();
    }
}