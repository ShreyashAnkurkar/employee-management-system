package com.nit.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nit.dto.request.PayrollRequest;
import com.nit.dto.response.PayrollResponse;
import com.nit.service.PayrollService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/payrolls")
@RequiredArgsConstructor
public class PayrollController {

    private final PayrollService payrollService;

    @PostMapping
    public ResponseEntity<PayrollResponse> generatePayroll(@Valid @RequestBody PayrollRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(payrollService.generatePayroll(request));
    }

    @GetMapping
    public ResponseEntity<List<PayrollResponse>> getAllPayrolls() {
        return ResponseEntity.ok(payrollService.getAllPayrolls());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PayrollResponse> getPayrollById(@PathVariable Long id) {
        return ResponseEntity.ok(payrollService.getPayrollById(id));
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<PayrollResponse>> getPayrollsByEmployee(@PathVariable Long employeeId) {
        return ResponseEntity.ok(payrollService.getPayrollsByEmployee(employeeId));
    }

    @GetMapping("/month/{month}/year/{year}")
    public ResponseEntity<List<PayrollResponse>> getPayrollsByMonthAndYear(@PathVariable Integer month,
                                                                            @PathVariable Integer year) {
        return ResponseEntity.ok(payrollService.getPayrollsByMonthAndYear(month, year));
    }

    @PutMapping("/{id}/mark-paid")
    public ResponseEntity<PayrollResponse> markAsPaid(@PathVariable Long id) {
        return ResponseEntity.ok(payrollService.markAsPaid(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePayroll(@PathVariable Long id) {
        payrollService.deletePayroll(id);
        return ResponseEntity.ok("Payroll deleted successfully");
    }
}