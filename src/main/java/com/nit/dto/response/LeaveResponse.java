package com.nit.dto.response;

import java.time.LocalDate;

import com.nit.entity.Leave.LeaveStatus;
import com.nit.entity.Leave.LeaveType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeaveResponse {
    private Long id;
    private Long employeeId;
    private String employeeName;
    private LocalDate startDate;
    private LocalDate endDate;
    private String reason;
    private LeaveType leaveType;
    private LeaveStatus status;
    private String rejectionReason;
    private long totalDays;
}