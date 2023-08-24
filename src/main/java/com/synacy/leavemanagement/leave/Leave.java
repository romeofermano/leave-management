package com.synacy.leavemanagement.leave;

import com.synacy.leavemanagement.enums.LeaveStatus;
import com.synacy.leavemanagement.employee.Employee;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Entity
@Table(name = "leave")
public class Leave {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "leave_sequence")
    @SequenceGenerator(name = "leave_sequence", sequenceName = "leave_sequence", allocationSize = 1)
    @Column(nullable = false)
    Long id;

    @Setter
    @ManyToOne(targetEntity = Employee.class, cascade = {CascadeType.ALL})
    @JoinColumn(name = "employee_id")
    Employee employee;

    @Setter
    @Column(nullable = false)
    LocalDate startDate;

    @Setter
    @Column(nullable = false)
    LocalDate endDate;

    @Setter
    @Column(nullable = false)
    Integer days;

    @Setter
    @Column(nullable = false)
    String reason;

    @Setter
    @Column(nullable = false)
    LeaveStatus leaveStatus;

    public Leave(){

    }
}
