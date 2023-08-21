package com.synacy.leavemanagement.leave;

import com.synacy.leavemanagement.employee.model.Employee;
import com.synacy.leavemanagement.enums.LeaveStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
public class Leave {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "leave_sequence")
    @SequenceGenerator(name = "leave_sequence", sequenceName = "leave_sequence", allocationSize = 1)
    @Column(nullable = false)
    Long id;

    @Column(nullable = false)
    @ManyToOne(targetEntity = Employee.class, cascade = {CascadeType.ALL})
    @JoinColumn(name = "employee_id")
    Employee employee;

    @Column(nullable = false)
    LocalDate startDate;

    @Column(nullable = false)
    LocalDate endDate;

    @Column(nullable = false)
    Integer days;

    @Column(nullable = false)
    String reason;

    @Column(nullable = false)
    LeaveStatus leaveStatus;

    public Leave(){

    }
}
