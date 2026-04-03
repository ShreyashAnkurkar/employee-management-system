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

import com.nit.dto.request.LeaveRequest;
import com.nit.dto.request.LeaveStatusRequest;
import com.nit.dto.response.LeaveResponse;
import com.nit.service.LeaveService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/leaves")
@RequiredArgsConstructor
public class LeaveController {

    private final LeaveService leaveService;

    @PostMapping
    public ResponseEntity<LeaveResponse> applyLeave(@Valid @RequestBody LeaveRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(leaveService.applyLeave(request));
    }

    @GetMapping
    public ResponseEntity<List<LeaveResponse>> getAllLeaves() {
        return ResponseEntity.ok(leaveService.getAllLeaves());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LeaveResponse> getLeaveById(@PathVariable Long id) {
        return ResponseEntity.ok(leaveService.getLeaveById(id));
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<LeaveResponse>> getLeavesByEmployee(@PathVariable Long employeeId) {
        return ResponseEntity.ok(leaveService.getLeavesByEmployee(employeeId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<LeaveResponse>> getLeavesByStatus(@PathVariable String status) {
        return ResponseEntity.ok(leaveService.getLeavesByStatus(status));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<LeaveResponse> updateLeaveStatus(@PathVariable Long id,
                                                            @Valid @RequestBody LeaveStatusRequest request) {
        return ResponseEntity.ok(leaveService.updateLeaveStatus(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteLeave(@PathVariable Long id) {
        leaveService.deleteLeave(id);
        return ResponseEntity.ok("Leave deleted successfully");
    }
}