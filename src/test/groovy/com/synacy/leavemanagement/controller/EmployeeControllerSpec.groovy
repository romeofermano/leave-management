package com.synacy.leavemanagement.controller

import com.synacy.leavemanagement.enums.RoleType
import com.synacy.leavemanagement.model.Employee
import com.synacy.leavemanagement.repository.EmployeeRepository
import com.synacy.leavemanagement.response.EmployeeManagerResponse
import com.synacy.leavemanagement.response.PageResponse
import com.synacy.leavemanagement.services.EmployeeService
import org.springframework.http.HttpStatus
import spock.lang.Specification

class EmployeeControllerSpec extends Specification {

    EmployeeController employeeController
    EmployeeService employeeService

    void setup() {
        employeeService = Mock(EmployeeService)
        employeeController = new EmployeeController(employeeService)
    }

    def "getEmployees should return pages of employees"() {
        given:
        int max = 5
        int page = 1
        int totalCount = 1
        List<Employee> employee = [new Employee("Employee 1", RoleType.MANAGER, 10)]
        def expectedResponse = new PageResponse<>(totalCount, page, [new EmployeeManagerResponse(employee[0])])

        employeeService.fetchEmployees(max, page) >> employee

        when:
        PageResponse<EmployeeManagerResponse> response = employeeController.getEmployees(max, page)

        then:
        response.totalCount == expectedResponse.totalCount
        response.pageNumber == expectedResponse.pageNumber
        response.content == expectedResponse.content
    }

    def "GetListEmployees"() {
    }

    def "CreateEmployee"() {
    }
}
