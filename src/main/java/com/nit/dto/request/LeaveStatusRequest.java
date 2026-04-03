package com.nit.dto.request;

import com.nit.entity.Leave.LeaveStatus;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeaveStatusRequest {

    @NotNull(message = "Status is required")
    private LeaveStatus status;

    private String rejectionReason;
}