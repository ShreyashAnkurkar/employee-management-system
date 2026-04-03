package com.nit.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nit.entity.Payroll;

@Repository
public interface PayrollRepository extends JpaRepository<Payroll, Long> {
    List<Payroll> findByEmployeeId(Long employeeId);
    List<Payroll> findByPayMonthAndPayYear(Integer month, Integer year);
    Optional<Payroll> findByEmployeeIdAndPayMonthAndPayYear(Long employeeId, Integer month, Integer year);
    boolean existsByEmployeeIdAndPayMonthAndPayYear(Long employeeId, Integer month, Integer year);
}