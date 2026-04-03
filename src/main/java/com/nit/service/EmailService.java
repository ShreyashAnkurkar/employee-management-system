package com.nit.service;

public interface EmailService {
    void sendLeaveStatusEmail(String toEmail, String employeeName, String leaveStatus, String reason);
    void sendPayslipEmail(String toEmail, String employeeName, Double netSalary, Integer month, Integer year);
    void sendWelcomeEmail(String toEmail, String employeeName, String username);
}