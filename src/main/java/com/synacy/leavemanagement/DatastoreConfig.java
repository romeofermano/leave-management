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
        Employee manager = new Employee("Romeo", RoleType.MANAGER, 30, admin);
        Employee member = new Employee("Ernest", RoleType.MEMBER, 30, manager);

        List<Employee> employeeList = new ArrayList<>();
        employeeList.add(admin);
        employeeList.add(manager);
        employeeList.add(member);

        return employeeList;
    }
}
