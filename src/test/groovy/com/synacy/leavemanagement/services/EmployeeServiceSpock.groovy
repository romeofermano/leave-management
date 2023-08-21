package com.synacy.leavemanagement.services

import com.synacy.leavemanagement.enums.EmployeeStatus
import com.synacy.leavemanagement.enums.RoleType
import com.synacy.leavemanagement.model.Employee
import com.synacy.leavemanagement.repository.EmployeeRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import spock.lang.Specification

class EmployeeServiceSpock extends Specification {

    EmployeeService employeeService
    EmployeeRepository employeeRepository

    void setup() {
        employeeRepository = Mock(EmployeeRepository)
        employeeService = new EmployeeService(employeeRepository)
    }

    def "fetchEmployees should fetch all employee"() {
        given:
        int max = 3
        int page = 1
        int expectedCount = 4
        Long managerId = 1L
        RoleType roleTypeManager = RoleType.MANAGER
        RoleType roleTypeMember = RoleType.MEMBER
        Page<Employee> employees = new PageImpl<>([new Employee("Employee 1", roleTypeManager, 25),
                                                   new Employee("Employee 2", roleTypeMember, 20, Mock(Employee)),
                                                   new Employee("Employee 3", roleTypeManager, 25),
                                                   new Employee("Employee 2", roleTypeMember, 20, Mock(Employee))])

        when:
        Page<Employee> result = employeeService.fetchEmployees(max, page)

        then:
        1 * employeeRepository.findAllByEmployeeStatus(EmployeeStatus.ACTIVE, _) >> employees
        employees == result
        result.getSize() == expectedCount
    }
}
