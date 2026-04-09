package com.nit.dto.request;

import java.time.LocalDate;

import com.nit.entity.Leave.LeaveType;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeaveRequest {

	private Long employeeId;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    private LocalDate endDate;

    private String reason;

    @NotNull(message = "Leave type is required")
    private LeaveType leaveType;
}