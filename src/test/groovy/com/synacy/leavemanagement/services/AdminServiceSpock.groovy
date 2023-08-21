package com.synacy.leavemanagement.services

import com.synacy.leavemanagement.employee.model.request.EmployeeRequest
import com.synacy.leavemanagement.employee.model.services.AdminService
import com.synacy.leavemanagement.enums.EmployeeStatus
import com.synacy.leavemanagement.enums.RoleType
import com.synacy.leavemanagement.employee.model.Employee
import com.synacy.leavemanagement.employee.model.repository.EmployeeRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import spock.lang.Specification

class AdminServiceSpock extends Specification {

    AdminService employeeService
    EmployeeRepository employeeRepository

    void setup() {
        employeeRepository = Mock(EmployeeRepository)
        employeeService = new AdminService(employeeRepository)
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

    def "createMember should create new member with the correct values when the creator is hr_admin"() {
        given:
        Long adminId = 1L
        Employee employeeAdmin = new Employee("Admin")
        EmployeeRequest managerRequest = new EmployeeRequest(name: "Robot", totalLeaves: 10,
                roleType: RoleType.MANAGER)

        and:
        employeeRepository.findByIdAndRoleType(adminId, RoleType.HR_ADMIN) >> Optional.of(employeeAdmin)

        when:
        employeeService.createMember(adminId, managerRequest)

        then:
        1 * employeeRepository.save(_) >> { Employee savedEmployee ->
            assert managerRequest.getName() == savedEmployee.getName()
            assert managerRequest.getTotalLeaves() == savedEmployee.getTotalLeaves()
            assert managerRequest.getRoleType() == savedEmployee.getRoleType()
            assert EmployeeStatus.ACTIVE == savedEmployee.getEmployeeStatus()
        }
    }
}
