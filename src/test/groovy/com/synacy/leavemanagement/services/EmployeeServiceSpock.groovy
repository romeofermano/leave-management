package com.synacy.leavemanagement.services

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
        Page<Employee> employees = new PageImpl<>([Mock(Employee), Mock(Employee), Mock(Employee), Mock(Employee)])

        when:
        Page<Employee> result = employeeService.fetchEmployees(max, page)

        then:
        1 * employeeRepository.findAll(_) >> employees
        employees == result
        result.getSize() == expectedCount
    }
}
