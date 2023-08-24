package com.synacy.leavemanagement.services

import com.synacy.leavemanagement.employee.EmployeeService
import com.synacy.leavemanagement.enums.EmployeeStatus
import com.synacy.leavemanagement.enums.RoleType
import com.synacy.leavemanagement.employee.Employee
import com.synacy.leavemanagement.employee.EmployeeRepository
import com.synacy.leavemanagement.employee.request.EmployeeManagerRequest
import com.synacy.leavemanagement.employee.request.EmployeeMemberRequest
import com.synacy.leavemanagement.web.exceptions.InvalidAdminException
import com.synacy.leavemanagement.web.exceptions.UserNotFoundException
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
        Employee employee = new Employee("Member", RoleType.MEMBER, 10, Mock(Employee))

        when:
        Employee result = employeeService.fetchEmployeeById(employeeId)

        then:
        1 * employeeRepository.findByIdAndEmployeeStatusAndRoleTypeIn(employeeId, EmployeeStatus.ACTIVE) >> Optional.of(employee)
        result == employee
    }

    def "fetchEmployees should fetch all employee"() {
        given:
        int max = 3
        int page = 1
        RoleType roleTypeManager = RoleType.MANAGER
        RoleType roleTypeMember = RoleType.MEMBER
        Page<Employee> employees = new PageImpl<>([new Employee("Employee 1", roleTypeManager, 25, Mock(Employee)),
                                                   new Employee("Employee 2", roleTypeMember, 20, Mock(Employee)),
                                                   new Employee("Employee 3", roleTypeManager, 25, Mock(Employee)),
                                                   new Employee("Employee 2", roleTypeMember, 20, Mock(Employee))])

        when:
        Page<Employee> result = employeeService.fetchEmployees(max, page)

        then:
        1 * employeeRepository.findAllByEmployeeStatusAndRoleTypeIn(EmployeeStatus.ACTIVE,
                [RoleType.MANAGER, RoleType.MEMBER], _) >> employees
        employees == result
    }

    def "fetchListEmployee should fetch list of employee with the employee status is active"() {
        given:
        List<Employee> employees = [new Employee("Member 1", RoleType.MEMBER, 10, Mock(Employee)),
                                    new Employee("Manager 1", RoleType.MANAGER, 10, Mock(Employee)),
                                    new Employee("Admin 1")]

        when:
        List<Employee> result = employeeService.fetchListEmployee()

        then:
        1 * employeeRepository.findAllByEmployeeStatus(EmployeeStatus.ACTIVE) >> employees
        result[0] == employees[0]
        result[1] == employees[1]
        result[2] == employees[2]
    }

    def "createEmployeeManager should create new manager with the correct values when the creator is HR Admin"() {
        given:
        Long adminId = 1L
        Employee employeeAdmin = new Employee("Admin")
        EmployeeManagerRequest employeeRequest = new EmployeeManagerRequest(name: "Robot", totalLeaves: 10,
                roleType: RoleType.MANAGER)

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
        Employee employee = new Employee("Employee 1", RoleType.MANAGER, 15, Mock(Employee))
        EmployeeManagerRequest managerRequest = Mock(EmployeeManagerRequest)

        when:
        employeeService.createEmployeeManager(id, managerRequest)

        then:
        1 * employeeRepository.findByIdAndEmployeeStatusAndRoleType(id, EmployeeStatus.ACTIVE, RoleType.HR_ADMIN) >> Optional.of(employee)
        thrown(InvalidAdminException)
    }

    def "createEmployeeMember should create new member with the correct values when the creator is HR Admin"() {
        given:
        Long adminId = 1L
        Long managerId = 3L
        Employee admin = new Employee("Admin")
        Employee manager = new Employee("Manager", RoleType.MANAGER, 10, Mock(Employee))
        EmployeeMemberRequest memberRequest = new EmployeeMemberRequest(name: "Member 1", roleType: RoleType.MEMBER,
                totalLeaves: 15, managerId: managerId)

        and:
        employeeRepository.findByIdAndEmployeeStatusAndRoleType(adminId, EmployeeStatus.ACTIVE, RoleType.HR_ADMIN) >> Optional.of(admin)
        employeeRepository.findByIdAndEmployeeStatusAndRoleType(managerId, EmployeeStatus.ACTIVE, RoleType.MANAGER) >> Optional.of(manager)

        when:
        employeeService.createEmployeeMember(adminId, memberRequest)

        then:
        1 * employeeRepository.save(_) >> { Employee savedMember ->
            memberRequest.getName() == savedMember.getName()
            memberRequest.getRoleType() == savedMember.getRoleType()
            memberRequest.getTotalLeaves() == savedMember.getTotalLeaves()
            memberRequest.getManagerId() == savedMember.getManager().getId()
            EmployeeStatus.ACTIVE == savedMember.getEmployeeStatus()
            0 == savedMember.getCurrentLeaves()
        }
    }

    def "createEmployeeMember should throw InvalidAdminException when the creator is not HR Admin"() {
        given:
        Long adminId = 1L
        EmployeeMemberRequest memberRequest = Mock(EmployeeMemberRequest)
        Employee employee = new Employee("Employee 1", RoleType.MANAGER, 15, Mock(Employee))

        and:
        employeeRepository.findByIdAndEmployeeStatusAndRoleType(adminId, EmployeeStatus.ACTIVE, RoleType.HR_ADMIN) >> Optional.of(employee)

        when:
        employeeService.createEmployeeMember(adminId, memberRequest)

        then:
        thrown(InvalidAdminException)
    }

    def "createEmployeeMember should throw UserNotFoundException when the manager does not exist"() {
        given:
        Long adminId = 1L
        Long managerId = 3L
        Employee employeeAdmin = new Employee("HR Admin")
        EmployeeMemberRequest memberRequest = new EmployeeMemberRequest(name: "Member 1", roleType: RoleType.MEMBER,
                totalLeaves: 10, managerId: managerId)

        and:
        employeeRepository.findByIdAndEmployeeStatusAndRoleType(adminId, EmployeeStatus.ACTIVE, RoleType.HR_ADMIN) >> Optional.of(employeeAdmin)
        employeeRepository.findByIdAndEmployeeStatusAndRoleType(managerId, EmployeeStatus.ACTIVE, RoleType.MANAGER) >> Optional.empty()

        when:
        employeeService.createEmployeeMember(adminId, memberRequest)

        then:
        thrown(UserNotFoundException)
    }

    def "terminateEmployee should terminate existing employee when the user is HR Admin"() {
        given:
        Long adminId = 1L
        Long employeeId = 3L
        Employee admin = new Employee("Admin")
        Employee existingEmployee = new Employee("Manager", RoleType.MEMBER, 10, Mock(Employee))

        and:
        employeeRepository.findByIdAndEmployeeStatusAndRoleType(adminId, EmployeeStatus.ACTIVE, RoleType.HR_ADMIN) >> Optional.of(admin)
        employeeRepository.findByIdAndEmployeeStatusAndRoleTypeIn(employeeId, EmployeeStatus.ACTIVE,
                [RoleType.MEMBER, RoleType.MANAGER]) >> Optional.of(existingEmployee)

        when:
        employeeService.terminateEmployee(adminId, employeeId)

        then:
        1 * employeeRepository.save(_) >> { Employee deletedEmployee ->
            assert existingEmployee.getName() == deletedEmployee.getName()
            assert existingEmployee.getRoleType() == deletedEmployee.getRoleType()
            assert existingEmployee.getTotalLeaves() == deletedEmployee.getTotalLeaves()
            assert existingEmployee.getManager() == deletedEmployee.getManager()
            assert EmployeeStatus.TERMINATED == deletedEmployee.getEmployeeStatus()
        }
    }

    def "terminateEmployee should throw InvalidAdminException when user is not HR Admin"() {
        given:
        Long adminId = 1L
        Long employeeId = 2L
        Employee employee = new Employee("Manager", RoleType.MEMBER, 10, Mock(Employee))

        and:
        employeeRepository.findByIdAndEmployeeStatusAndRoleType(adminId, EmployeeStatus.ACTIVE, RoleType.HR_ADMIN) >> Optional.of(employee)

        when:
        employeeService.terminateEmployee(adminId, employeeId)

        then:
        thrown(InvalidAdminException)
    }

    def "terminateEmployee should throw UserNotFoundException when the employee is doesn't exist"() {
        given:
        Long adminId = 1L
        Long employeeId = 2L
        Employee admin = new Employee("HR Admin")

        and:
        employeeRepository.findByIdAndEmployeeStatusAndRoleType(adminId, EmployeeStatus.ACTIVE, RoleType.HR_ADMIN) >> Optional.of(admin)
        employeeRepository.findByIdAndEmployeeStatusAndRoleTypeIn(_ as Long, EmployeeStatus.ACTIVE, _ as Collection<RoleType>) >> Optional.empty()

        when:
        employeeService.terminateEmployee(adminId, employeeId)

        then:
        thrown(UserNotFoundException)
    }
}
