package com.synacy.leavemanagement;

import com.synacy.leavemanagement.employee.Employee;
import com.synacy.leavemanagement.enums.RoleType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class DatastoreConfig {

    @Bean
    public List<Employee> getEmployeeList() {
        Employee admin = new Employee("HR Admin", RoleType.HR_ADMIN, null, null);
        Employee manager1 = new Employee("Romeo Fermano", RoleType.MANAGER, 30, admin);
        Employee manager2 = new Employee("Clark Tabar", RoleType.MANAGER, 30, admin);
        Employee manager3 = new Employee("Carlina Amaba", RoleType.MANAGER, 30, admin);
        Employee member1 = new Employee("Ernest Dylan Gloria", RoleType.MEMBER, 30, manager1);
        Employee member2 = new Employee("Junsaku Yamada", RoleType.MEMBER, 30, manager2);
        Employee member3 = new Employee("Precious Mae Marson", RoleType.MEMBER, 30, manager3);

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
}
