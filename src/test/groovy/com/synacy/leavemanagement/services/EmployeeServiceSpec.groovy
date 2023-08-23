package com.synacy.leavemanagement.services

import com.synacy.leavemanagement.enums.EmployeeStatus
import com.synacy.leavemanagement.enums.RoleType
import com.synacy.leavemanagement.model.Employee
import com.synacy.leavemanagement.repository.EmployeeRepository
import com.synacy.leavemanagement.request.EmployeeManagerRequest
import com.synacy.leavemanagement.web.exceptions.InvalidAdminException
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import spock.lang.Specification

class EmployeeServiceSpec extends Specification {

    EmployeeService employeeService
    EmployeeRepository employeeRepository

    void setup() {
        employeeRepository = Mock(EmployeeRepository)
        employeeService = new EmployeeService(employeeRepository)
    }

    def "fetchTotalEmployee should count all employee that is currently active"() {
        given:
        int expectedCount = 5

        and:
        employeeRepository.countAllByEmployeeStatusAndRoleTypeIn(EmployeeStatus.ACTIVE,
                [RoleType.MEMBER, RoleType.MANAGER]) >> expectedCount

        when:
        int result = employeeService.fetchTotalEmployee()

        then:
        expectedCount == result
    }

    def "fetchEmployeeById should fetch employee by id with the employee status is active"() {
        given:
        Long employeeId = 1L
        Employee employee = new Employee("Member", RoleType.MEMBER, 10)

        when:
        Employee result = employeeService.fetchEmployeeById(employeeId)

        then:
        1 * employeeRepository.findByIdAndEmployeeStatus(employeeId, EmployeeStatus.ACTIVE) >> Optional.of(employee)
        result == employee
    }

    def "fetchEmployees should fetch all employee"() {
        given:
        int max = 3
        int page = 1
        RoleType roleTypeManager = RoleType.MANAGER
        RoleType roleTypeMember = RoleType.MEMBER
        Page<Employee> employees = new PageImpl<>([new Employee("Employee 1", roleTypeManager, 25),
                                                   new Employee("Employee 2", roleTypeMember, 20),
                                                   new Employee("Employee 3", roleTypeManager, 25),
                                                   new Employee("Employee 2", roleTypeMember, 20)])

        when:
        Page<Employee> result = employeeService.fetchEmployees(max, page)

        then:
        1 * employeeRepository.findAllByEmployeeStatusAndRoleTypeIn(EmployeeStatus.ACTIVE,
                [RoleType.MANAGER, RoleType.MEMBER], _) >> employees
        employees == result
    }

    def "fetchListEmployee should fetch list of employee with the employee status is active"() {
        given:
        List<Employee> employees = [new Employee("Member 1", RoleType.MEMBER, 10),
                                    new Employee("Manager 1", RoleType.MANAGER, 10),
                                    new Employee("Admin 1")]

        when:
        List<Employee> result = employeeService.fetchListEmployee()

        then:
        1 * employeeRepository.findAllByEmployeeStatus(EmployeeStatus.ACTIVE) >> employees
        result.containsAll(employees)
    }

    def "createEmployeeManager should create new member with the correct values when the creator is HR Admin"() {
        given:
        Long adminId = 1L
        Long managerId = 6L
        Employee employeeAdmin = new Employee("Admin")
        Employee manager = new Employee("Manager 1", RoleType.MANAGER, 10)
        EmployeeManagerRequest employeeRequest = new EmployeeManagerRequest(name: "Robot", totalLeaves: 10,
                roleType: RoleType.MEMBER)

        and:
        employeeRepository.findByIdAndEmployeeStatusAndRoleType(adminId, EmployeeStatus.ACTIVE, RoleType.HR_ADMIN) >> Optional.of(employeeAdmin)

        when:
        employeeService.createEmployeeManager(adminId, employeeRequest)

        then:
        1 * employeeRepository.save(_) >> { Employee savedEmployee ->
            assert employeeRequest.getName() == savedEmployee.getName()
            assert employeeRequest.getTotalLeaves() == savedEmployee.getTotalLeaves()
            assert employeeRequest.getRoleType() == savedEmployee.getRoleType()
            assert EmployeeStatus.ACTIVE == savedEmployee.getEmployeeStatus()
        }
    }

    def "createEmployeeManager should throw InvalidAdminException when creating a new member is not HR_ADMIN"() {
        given:
        Long id = 1L
        Employee employee = new Employee("Employee 1", RoleType.MANAGER, 15)
        EmployeeManagerRequest managerRequest = Mock(EmployeeManagerRequest)

        when:
        employeeService.createEmployeeManager(id, managerRequest)

        then:
        1 * employeeRepository.findByIdAndEmployeeStatusAndRoleType(id, EmployeeStatus.ACTIVE, RoleType.HR_ADMIN) >> Optional.of(employee)
        thrown(InvalidAdminException)
    }
}
