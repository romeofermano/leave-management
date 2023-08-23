package com.synacy.leavemanagement.controller

import com.synacy.leavemanagement.services.EmployeeService
import spock.lang.Specification

class EmployeeControllerSpec extends Specification {

    EmployeeController employeeController
    EmployeeService employeeService

    void setup() {
        employeeService = Mock(EmployeeService)
        employeeController = new EmployeeController(employeeService)
    }

    def "getEmployees should return pages of employees"() {
    }

    def "GetListEmployees"() {
    }

    def "CreateEmployee"() {
    }
}
