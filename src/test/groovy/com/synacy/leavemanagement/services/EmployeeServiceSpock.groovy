package com.synacy.leavemanagement.services

import com.synacy.leavemanagement.employee.model.request.EmployeeManagerRequest
import com.synacy.leavemanagement.employee.model.services.EmployeeService
import com.synacy.leavemanagement.enums.EmployeeStatus
import com.synacy.leavemanagement.enums.RoleType
import com.synacy.leavemanagement.employee.model.Employee
import com.synacy.leavemanagement.employee.model.repository.EmployeeRepository
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
        RoleType roleTypeManager = RoleType.MANAGER
        RoleType roleTypeMember = RoleType.MEMBER
        Page<Employee> employees = new PageImpl<>([new Employee("Employee 1", roleTypeManager, 25),
                                                   new Employee("Employee 2", roleTypeMember, 20,
                                                           Mock(Employee)),
                                                   new Employee("Employee 3", roleTypeManager, 25),
                                                   new Employee("Employee 2", roleTypeMember, 20,
                                                           Mock(Employee))])

        when:
        Page<Employee> result = employeeService.fetchEmployees(max, page)

        then:
        1 * employeeRepository.findAllByEmployeeStatusAndRoleTypeIn(EmployeeStatus.ACTIVE,
                [RoleType.MANAGER, RoleType.MEMBER], _) >> employees
        employees == result
    }

    def "createManager should create new manager with the correct values"() {
        given:
        EmployeeManagerRequest managerRequest = new EmployeeManagerRequest(name: "Robot", totalLeaves: 10,
                roleType: RoleType.MANAGER)

        when:
        employeeService.createManager(managerRequest)

        then:
        1 * employeeRepository.save(_) >> { Employee savedEmployee ->
            assert managerRequest.getName() == savedEmployee.getName()
            assert managerRequest.getTotalLeaves() == savedEmployee.getTotalLeaves()
            assert managerRequest.getRoleType() == savedEmployee.getRoleType()
            assert EmployeeStatus.ACTIVE == savedEmployee.getEmployeeStatus()
        }
    }
}
