package com.nit.service;

import java.util.List;

import com.nit.dto.request.PayrollRequest;
import com.nit.dto.response.PayrollResponse;

public interface PayrollService {
    PayrollResponse generatePayroll(PayrollRequest request);
    PayrollResponse getPayrollById(Long id);
    List<PayrollResponse> getAllPayrolls();
    List<PayrollResponse> getPayrollsByEmployee(Long employeeId);
    List<PayrollResponse> getPayrollsByMonthAndYear(Integer month, Integer year);
    PayrollResponse markAsPaid(Long id);
    void deletePayroll(Long id);
}