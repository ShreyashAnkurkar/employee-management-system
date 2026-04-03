package com.nit.service;

import java.util.List;

import com.nit.dto.request.LeaveRequest;
import com.nit.dto.request.LeaveStatusRequest;
import com.nit.dto.response.LeaveResponse;

public interface LeaveService {
    LeaveResponse applyLeave(LeaveRequest request);
    LeaveResponse getLeaveById(Long id);
    List<LeaveResponse> getAllLeaves();
    List<LeaveResponse> getLeavesByEmployee(Long employeeId);
    List<LeaveResponse> getLeavesByStatus(String status);
    LeaveResponse updateLeaveStatus(Long id, LeaveStatusRequest request);
    void deleteLeave(Long id);
}