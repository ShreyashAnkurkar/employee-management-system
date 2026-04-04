package com.nit.service.impl;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.nit.service.EmailService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendLeaveStatusEmail(String toEmail, String employeeName,
                                      String leaveStatus, String reason) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setFrom("ankurkarshreyash07@gmail.com");
        message.setSubject("Leave Request " + leaveStatus);
        message.setText(
            "Dear " + employeeName + ",\n\n" +
            "Your leave request has been " + leaveStatus + ".\n" +
            (reason != null ? "Reason: " + reason + "\n" : "") +
            "\nRegards,\nHR Team"
        );
        mailSender.send(message);
    }

    @Override
    public void sendPayslipEmail(String toEmail, String employeeName,
                                  Double netSalary, Integer month, Integer year) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setFrom("ankurkarshreyash07@gmail.com");
        message.setSubject("Payslip for " + month + "/" + year);
        message.setText(
            "Dear " + employeeName + ",\n\n" +
            "Your payslip for " + month + "/" + year + " has been generated.\n" +
            "Net Salary: ₹" + netSalary + "\n\n" +
            "Regards,\nHR Team"
        );
        mailSender.send(message);
    }

    @Override
    public void sendWelcomeEmail(String toEmail, String employeeName, String username) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setFrom("ankurkarshreyash07@gmail.com");
        message.setSubject("Welcome to Employee Management System");
        message.setText(
            "Dear " + employeeName + ",\n\n" +
            "Welcome! Your account has been created.\n" +
            "Username: " + username + "\n\n" +
            "Please login and change your password.\n\n" +
            "Regards,\nHR Team"
        );
        mailSender.send(message);
    }
}