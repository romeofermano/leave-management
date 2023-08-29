package com.synacy.leavemanagement.leave;

import com.synacy.leavemanagement.employee.Employee;
import com.synacy.leavemanagement.enums.LeaveStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

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

    public Leave(Employee employee, LocalDate startDate, LocalDate endDate, String reason) {
        this.employee = employee;
        this.startDate = startDate;
        this.endDate = endDate;
        this.days = (int) daysDifference(startDate, endDate);
        this.reason = reason;
    }

    public Leave(){

    }

    private long daysDifference(LocalDate startDate, LocalDate endDate){
        long daysDifference = 0;
        LocalDate currentDate = startDate;

        while (!currentDate.isAfter(endDate)) {
            if ((currentDate.getDayOfWeek() != DayOfWeek.SATURDAY) && (currentDate.getDayOfWeek() != DayOfWeek.SUNDAY)) {
                daysDifference++;
            }

            currentDate = currentDate.plusDays(1);
        }

        return daysDifference;
    }

    void cancel(){
        this.setLeaveStatus(LeaveStatus.CANCELLED);
    }
}
