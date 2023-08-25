package com.synacy.leavemanagement.employee;

import com.synacy.leavemanagement.enums.EmployeeStatus;
import com.synacy.leavemanagement.enums.RoleType;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "employees")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "employee_sequence")
    @SequenceGenerator(name = "employee_sequence", sequenceName = "employee_sequence", allocationSize = 1)
    @Column(nullable = false, updatable = false)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    private Integer totalLeaves;

    private Integer currentLeaves;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EmployeeStatus employeeStatus;

    @ManyToOne(targetEntity = Employee.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "manager_id")
    private Employee manager;

    public Employee() {

    }

    public Employee(String name, RoleType roleType, Integer totalLeaves, Employee manager) {
        this.name = name;
        this.roleType = roleType;
        this.totalLeaves = totalLeaves;
        this.currentLeaves = 0;
        this.employeeStatus = EmployeeStatus.ACTIVE;
        this.manager = manager;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRoleType(RoleType roleType) {
        this.roleType = roleType;
    }

    public void setTotalLeaves(Integer totalLeaves) {
        this.totalLeaves = totalLeaves;
    }

    public void setCurrentLeaves(Integer currentLeaves) {
        this.currentLeaves = currentLeaves;
    }

    public void setEmployeeStatus(EmployeeStatus employeeStatus) {
        this.employeeStatus = employeeStatus;
    }

    public void setManager(Employee manager) {
        this.manager = manager;
    }

    public void terminate() {
        this.employeeStatus = EmployeeStatus.TERMINATED;
    }

    public void deductLeave(Integer days) {
        this.setCurrentLeaves(this.getCurrentLeaves() - days);
    }

    public void addLeave(Integer days) {
        this.setCurrentLeaves(this.getCurrentLeaves() + days);
    }
}
