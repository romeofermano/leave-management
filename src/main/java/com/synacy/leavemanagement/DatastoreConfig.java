package com.synacy.leavemanagement;


import com.synacy.leavemanagement.employee.Employee;
import com.synacy.leavemanagement.enums.RoleType;
import com.synacy.leavemanagement.leave.Leave;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class DatastoreConfig {

    Employee admin;
    Employee manager1;
    Employee manager2;
    Employee manager3;
    Employee member1;
    Employee member2;
    Employee member3;

    @Bean
    public List<Employee> getEmployeeList() {
        admin = new Employee("HR Admin", RoleType.HR_ADMIN, null, null);
        manager1 = new Employee("Romeo Fermano", RoleType.MANAGER, 30, admin);
        manager2 = new Employee("Clark Tabar", RoleType.MANAGER, 30, admin);
        manager3 = new Employee("Carlina Amaba", RoleType.MANAGER, 30, admin);
        member1 = new Employee("Ernest Dylan Gloria", RoleType.MEMBER, 30, manager1);
        member2 = new Employee("Junsaku Yamada", RoleType.MEMBER, 30, manager2);
        member3 = new Employee("Precious Mae Marson", RoleType.MEMBER, 30, manager3);

        List<Employee> employeeList = new ArrayList<>();
        employeeList.add(admin);
        employeeList.add(manager1);
        employeeList.add(manager2);
        employeeList.add(manager3);
        employeeList.add(member1);
        employeeList.add(member2);
        employeeList.add(member3);

        return employeeList;
    }

    @Bean
    public List<Leave> getLeaveList() {
        Leave leave1 = new Leave(manager1, LocalDate.of(2022, Month.JANUARY, 24), LocalDate.of(2022, Month.JANUARY, 28), "Vacation Leave");
        Leave leave2 = new Leave(manager2, LocalDate.of(2022, Month.FEBRUARY, 17), LocalDate.of(2022, Month.FEBRUARY, 18), "Sick Leave");
        Leave leave3 = new Leave(manager3, LocalDate.of(2022, Month.MARCH, 28), LocalDate.of(2022, Month.MARCH, 31), "Vacation Leave");
        Leave leave4 = new Leave(member1, LocalDate.of(2022, Month.APRIL, 15), LocalDate.of(2022, Month.APRIL, 15), "Sick Leave");
        Leave leave5 = new Leave(member2, LocalDate.of(2022, Month.MAY, 9), LocalDate.of(2022, Month.MAY, 13), "Vacation Leave");
        Leave leave6 = new Leave(member3, LocalDate.of(2022, Month.DECEMBER, 26), LocalDate.of(2022, Month.DECEMBER, 30), "Vacation Leave");

        List<Leave> leaveList = new ArrayList<>();
        leaveList.add(leave1);
        leaveList.add(leave2);
        leaveList.add(leave3);
        leaveList.add(leave4);
        leaveList.add(leave5);
        leaveList.add(leave6);

        return leaveList;
    }
}
