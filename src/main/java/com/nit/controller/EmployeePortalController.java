package com.nit.controller;

import com.nit.dto.request.LeaveRequest;
import com.nit.dto.response.EmployeeResponse;
import com.nit.dto.response.LeaveResponse;
import com.nit.dto.response.PayrollResponse;
import com.nit.entity.User;
import com.nit.exception.ResourceNotFoundException;
import com.nit.repository.UserRepository;
import com.nit.service.LeaveService;
import com.nit.service.PayrollService;
import com.nit.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employee")
@RequiredArgsConstructor
public class EmployeePortalController {

    private final UserRepository userRepository;
    private final EmployeeService employeeService;
    private final LeaveService leaveService;
    private final PayrollService payrollService;

    // Helper to get current logged in employee id
    private Long getEmployeeId(Authentication auth) {
        User user = userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (user.getEmployee() == null) {
            throw new ResourceNotFoundException("No employee linked to this account");
        }
        return user.getEmployee().getId();
    }

    // Get my profile
    @GetMapping("/profile")
    public ResponseEntity<EmployeeResponse> getMyProfile(Authentication auth) {
        Long empId = getEmployeeId(auth);
        return ResponseEntity.ok(employeeService.getEmployeeById(empId));
    }

    // Get my leaves
    @GetMapping("/leaves")
    public ResponseEntity<List<LeaveResponse>> getMyLeaves(Authentication auth) {
        Long empId = getEmployeeId(auth);
        return ResponseEntity.ok(leaveService.getLeavesByEmployee(empId));
    }

    // Apply for leave
    @PostMapping("/leaves")
    public ResponseEntity<LeaveResponse> applyLeave(
            Authentication auth,
            @Valid @RequestBody LeaveRequest request) {
        Long empId = getEmployeeId(auth);
        request.setEmployeeId(empId); // Force use logged-in employee's ID
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(leaveService.applyLeave(request));
    }

    // Get my payslips
    @GetMapping("/payslips")
    public ResponseEntity<List<PayrollResponse>> getMyPayslips(Authentication auth) {
        Long empId = getEmployeeId(auth);
        return ResponseEntity.ok(payrollService.getPayrollsByEmployee(empId));
    }
}