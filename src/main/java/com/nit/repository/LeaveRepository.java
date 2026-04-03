package com.nit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nit.entity.Leave;
import com.nit.entity.Leave.LeaveStatus;

@Repository
public interface LeaveRepository extends JpaRepository<Leave, Long> {
    List<Leave> findByEmployeeId(Long employeeId);
    List<Leave> findByStatus(LeaveStatus status);
    List<Leave> findByEmployeeIdAndStatus(Long employeeId, LeaveStatus status);
}