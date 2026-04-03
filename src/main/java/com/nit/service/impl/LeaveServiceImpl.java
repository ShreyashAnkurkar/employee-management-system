package com.nit.service.impl;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.nit.dto.request.LeaveRequest;
import com.nit.dto.request.LeaveStatusRequest;
import com.nit.dto.response.LeaveResponse;
import com.nit.entity.Employee;
import com.nit.entity.Leave;
import com.nit.entity.Leave.LeaveStatus;
import com.nit.exception.ResourceNotFoundException;
import com.nit.repository.EmployeeRepository;
import com.nit.repository.LeaveRepository;
import com.nit.service.EmailService;
import com.nit.service.LeaveService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LeaveServiceImpl implements LeaveService {

    private final LeaveRepository leaveRepository;
    private final EmployeeRepository employeeRepository;
    private final EmailService emailService;

    @Override
    public LeaveResponse applyLeave(LeaveRequest request) {
        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + request.getEmployeeId()));

        if (request.getEndDate().isBefore(request.getStartDate())) {
            throw new RuntimeException("End date cannot be before start date");
        }

        Leave leave = Leave.builder()
                .employee(employee)
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .reason(request.getReason())
                .leaveType(request.getLeaveType())
                .status(LeaveStatus.PENDING)
                .build();

        Leave saved = leaveRepository.save(leave);
        return mapToResponse(saved);
    }

    @Override
    public LeaveResponse getLeaveById(Long id) {
        Leave leave = leaveRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Leave not found with id: " + id));
        return mapToResponse(leave);
    }

    @Override
    public List<LeaveResponse> getAllLeaves() {
        return leaveRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<LeaveResponse> getLeavesByEmployee(Long employeeId) {
        return leaveRepository.findByEmployeeId(employeeId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<LeaveResponse> getLeavesByStatus(String status) {
        LeaveStatus leaveStatus = LeaveStatus.valueOf(status.toUpperCase());
        return leaveRepository.findByStatus(leaveStatus)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public LeaveResponse updateLeaveStatus(Long id, LeaveStatusRequest request) {
        Leave leave = leaveRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Leave not found with id: " + id));

        leave.setStatus(request.getStatus());
        if (request.getRejectionReason() != null) {
            leave.setRejectionReason(request.getRejectionReason());
        }

        if (request.getStatus() == LeaveStatus.APPROVED) {
            Employee employee = leave.getEmployee();
            employee.setStatus(com.nit.entity.Employee.EmployeeStatus.ON_LEAVE);
            employeeRepository.save(employee);
        }

        Leave updated = leaveRepository.save(leave);

        // Send email notification
        try {
            emailService.sendLeaveStatusEmail(
                leave.getEmployee().getEmail(),
                leave.getEmployee().getFirstName() + " " + leave.getEmployee().getLastName(),
                request.getStatus().name(),
                request.getRejectionReason()
            );
        } catch (Exception e) {
            // Don't fail the request if email fails
            System.out.println("Email sending failed: " + e.getMessage());
        }

        return mapToResponse(updated);
    }

    @Override
    public void deleteLeave(Long id) {
        Leave leave = leaveRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Leave not found with id: " + id));
        leaveRepository.delete(leave);
    }

    private LeaveResponse mapToResponse(Leave leave) {
        long totalDays = ChronoUnit.DAYS.between(leave.getStartDate(), leave.getEndDate()) + 1;
        return LeaveResponse.builder()
                .id(leave.getId())
                .employeeId(leave.getEmployee().getId())
                .employeeName(leave.getEmployee().getFirstName() + " " + leave.getEmployee().getLastName())
                .startDate(leave.getStartDate())
                .endDate(leave.getEndDate())
                .reason(leave.getReason())
                .leaveType(leave.getLeaveType())
                .status(leave.getStatus())
                .rejectionReason(leave.getRejectionReason())
                .totalDays(totalDays)
                .build();
    }
}