package com.synacy.leavemanagement.employee


import com.synacy.leavemanagement.employee.request.EmployeeRequest
import com.synacy.leavemanagement.employee.response.EmployeeListResponse
import com.synacy.leavemanagement.employee.response.EmployeeResponse
import com.synacy.leavemanagement.enums.EmployeeStatus
import com.synacy.leavemanagement.enums.RoleType
import com.synacy.leavemanagement.web.exceptions.response.PageResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import spock.lang.Specification

class EmployeeControllerSpec extends Specification {

    EmployeeController employeeController
    EmployeeService employeeService

    void setup() {
        employeeService = Mock(EmployeeService)
        employeeController = new EmployeeController(employeeService)
    }

    def "getEmployees should fetch a PageResponse of EmployeeResponse with all employee"() {
        given:
        int max = 5
        int page = 1
        int expectedTotal = 2
        List<Employee> employeeList = [new Employee("Member 1", RoleType.MEMBER, 10, Mock(Employee)),
                                       new Employee("Manager 1", RoleType.MANAGER, 15, Mock(Employee))]
        Page<Employee> employeePage = new PageImpl<>(employeeList)

        and:
        employeeService.fetchEmployees(max, page) >> employeePage
        employeeService.fetchTotalEmployee() >> expectedTotal

        when:
        PageResponse<EmployeeResponse> result = employeeController.getEmployees(max, page)

        then:
        result.getTotalCount() == expectedTotal
        result.getPageNumber() == page
        result.getContent().size() == expectedTotal

        EmployeeResponse employeeResponse1 = result.content[0]
        employeeResponse1.id == employeeList[0].id
        employeeResponse1.name == employeeList[0].name
        employeeResponse1.roleType == employeeList[0].roleType
        employeeResponse1.totalLeaves == employeeList[0].totalLeaves
        employeeResponse1.manager == employeeList[0].manager.name
        employeeResponse1.currentLeaves == employeeList[0].totalLeaves
        employeeResponse1.employeeStatus == EmployeeStatus.ACTIVE

        EmployeeResponse employeeResponse2 = result.content[1]
        employeeResponse2.id == employeeList[1].id
        employeeResponse2.name == employeeList[1].name
        employeeResponse2.roleType == employeeList[1].roleType
        employeeResponse2.totalLeaves == employeeList[1].totalLeaves
        employeeResponse2.manager == employeeList[1].manager.name
        employeeResponse2.currentLeaves == employeeList[1].totalLeaves
        employeeResponse2.employeeStatus == EmployeeStatus.ACTIVE
    }

    def "getListEmployees should fetch a list of EmployeeListResponse"() {
        given:
        List<Employee> employeeList = [new Employee("Member 1", RoleType.MEMBER, 10, Mock(Employee)),
                                       new Employee("Manager 1", RoleType.MANAGER, 10, Mock(Employee))]
        int expectedTotalCount = 2

        employeeService.fetchListEmployee(null) >> employeeList

        when:
        List<EmployeeListResponse> result = employeeController.getListEmployees(null)

        then:
        result.size() == expectedTotalCount

        EmployeeListResponse employeeListResponse1 = result[0]
        employeeListResponse1.name == employeeList[0].name
        employeeListResponse1.roleType == employeeList[0].roleType

        EmployeeListResponse employeeListResponse2 = result[1]
        employeeListResponse2.name == employeeList[1].name
        employeeListResponse2.roleType == employeeList[1].roleType
    }

    def "createManager should create a manager then return the corresponding EmployeeResponse"() {
        given:
        Long adminId = 1L
        EmployeeRequest employeeRequest = new EmployeeRequest(name: "Manager", roleType: RoleType.MANAGER,
                totalLeaves: 10)
        Employee createdManager = new Employee("Manager", RoleType.MANAGER, 10, Mock(Employee))

        employeeService.createEmployee(adminId, employeeRequest) >> createdManager

        when:
        EmployeeResponse result = employeeController.createEmployee(adminId, employeeRequest)

        then:
        result.id == createdManager.id
        result.name == createdManager.name
        result.roleType == createdManager.roleType
        result.manager == createdManager.manager.name
        result.totalLeaves == createdManager.totalLeaves
        result.currentLeaves == createdManager.totalLeaves
        result.employeeStatus == EmployeeStatus.ACTIVE
    }

    def "createMember should create member then return the corresponding EmployeeResponse"() {

    }

    def "terminateEmployee should call employeeService.terminateEmployee with the correct parameters"() {
        given:
        Long adminId = 1L
        Long employeeId = 3L

        when:
        employeeController.terminateEmployee(adminId, employeeId)

        then:
        1 * employeeService.terminateEmployee(adminId, employeeId)
    }
}
