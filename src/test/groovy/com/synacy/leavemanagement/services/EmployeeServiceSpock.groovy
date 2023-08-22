package com.synacy.leavemanagement.services

import com.synacy.leavemanagement.request.EmployeeManagerRequest
import com.synacy.leavemanagement.request.EmployeeRequest
import com.synacy.leavemanagement.enums.EmployeeStatus
import com.synacy.leavemanagement.enums.RoleType
import com.synacy.leavemanagement.model.Employee
import com.synacy.leavemanagement.repository.EmployeeRepository
import com.synacy.leavemanagement.web.exceptions.InvalidAdminException
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

    def "createMember should create new member with the correct values when the creator is HR Admin"() {
        given:
        Long adminId = 1L
        Employee employeeAdmin = new Employee("Admin")
        EmployeeRequest employeeRequest = new EmployeeRequest(name: "Robot", totalLeaves: 10,
                roleType: RoleType.MANAGER)

        and:
        employeeRepository.findByIdAndEmployeeStatusAndRoleType(adminId,EmployeeStatus.ACTIVE, RoleType.HR_ADMIN) >> Optional.of(employeeAdmin)

        when:
        employeeService.createEmployee(adminId, employeeRequest)

        then:
        1 * employeeRepository.save(_) >> { Employee savedEmployee ->
            assert employeeRequest.getName() == savedEmployee.getName()
            assert employeeRequest.getTotalLeaves() == savedEmployee.getTotalLeaves()
            assert employeeRequest.getRoleType() == savedEmployee.getRoleType()
            assert EmployeeStatus.ACTIVE == savedEmployee.getEmployeeStatus()
        }
    }

    def "createMember should throw InvalidAdminException when creating a new member is not HR_ADMIN"() {
        given:
        Long id = 1L
        Employee employee = new Employee("Employee 1", RoleType.MANAGER, 15)
        EmployeeRequest employeeRequest = Mock(EmployeeRequest)

        when:
        employeeService.createEmployee(id, employeeRequest)

        then:
        1 * employeeRepository.findByIdAndEmployeeStatusAndRoleType(id, EmployeeStatus.ACTIVE, RoleType.HR_ADMIN) >> Optional.of(employee)
        thrown(InvalidAdminException)
    }

    def "createManager should create new manager with the correct values when the creator is HR Admin"() {
        given:
        Long id = 3L
        Employee employee = new Employee("Admin 1")
        EmployeeManagerRequest managerRequest = new EmployeeManagerRequest(name: "Manager 1",
                roleType: RoleType.MANAGER, totalLeaves: 10)

        when:
        employeeService.createManager(id, managerRequest)

        then:
        1 * employeeRepository.findByIdAndEmployeeStatusAndRoleType(id, EmployeeStatus.ACTIVE, RoleType.HR_ADMIN) >> Optional.of(employee)
        1 * employeeRepository.save(_) >> { Employee savedEmployee ->
            assert managerRequest.getName() == savedEmployee.getName()
            assert managerRequest.getRoleType() == savedEmployee.getRoleType()
            assert managerRequest.getTotalLeaves() == savedEmployee.getTotalLeaves()
            assert EmployeeStatus.ACTIVE == savedEmployee.getEmployeeStatus()
        }
    }

    def "createManager should throw InvalidAdminException when creating a new manager is not HR Admin"() {
        given:
        Long id = 2L
        Employee employee = new Employee("Member 1", RoleType.MEMBER, 20, Mock(Employee))
        EmployeeManagerRequest managerRequest = Mock(EmployeeManagerRequest)

        when:
        employeeService.createManager(id, managerRequest)

        then:
        1 * employeeRepository.findByIdAndEmployeeStatusAndRoleType(id, EmployeeStatus.ACTIVE,RoleType.HR_ADMIN) >> Optional.of(employee)
        thrown(InvalidAdminException)
    }

//    def "updateMember should update existing member when the user is HR Admin"() {
//        given:
//        Long adminId = 1L
//        Employee memberEmployee = new Employee("Member 1", RoleType.MEMBER, 10, Mock(Employee))
//        EmployeeRequest memberRequest = new EmployeeRequest(name: "Updated Member 1", roleType: RoleType.MANAGER, totalLeaves: 15)
//
//        when:
//        adminService.updateMember(adminId, memberRequest)
//    }
}
