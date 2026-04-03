package com.nit.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "payrolls")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payroll {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Employee employee;

    @Column(nullable = false)
    private Double basicSalary;

    @Column(nullable = false)
    private Double hra;           // House Rent Allowance (40% of basic)

    @Column(nullable = false)
    private Double allowances;    // Other allowances (20% of basic)

    @Column(nullable = false)
    private Double deductions;    // Tax + PF deductions (10% of basic)

    @Column(nullable = false)
    private Double netSalary;     // basicSalary + hra + allowances - deductions

    @Column(nullable = false)
    private LocalDate payDate;

    @Column(nullable = false)
    private Integer payMonth;     // 1-12

    @Column(nullable = false)
    private Integer payYear;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PayrollStatus status;

    public enum PayrollStatus {
        GENERATED, PAID
    }
}