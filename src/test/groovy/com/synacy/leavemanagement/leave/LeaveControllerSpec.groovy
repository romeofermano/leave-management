package com.synacy.leavemanagement.leave

import com.synacy.leavemanagement.employee.Employee
import com.synacy.leavemanagement.enums.LeaveStatus
import com.synacy.leavemanagement.enums.RoleType
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import spock.lang.Specification

import java.time.LocalDate

class LeaveControllerSpec extends Specification {
    LeaveController leaveController
    LeaveService leaveService

    void setup() {
        leaveService = Mock(LeaveService)
        leaveController = new LeaveController(leaveService)
    }

    def "fetchAllLeaves should return all leaves"() {
        given:
        int max = 3
        int page = 1
        Long managerId = 1L
        Employee manager = new Employee("Test Manager", RoleType.MANAGER, 30, Mock(Employee))
        manager.getId() >> managerId
        Employee employee = new Employee("Test Employee", RoleType.MEMBER, 30, manager)
        employee.getId() >> 2L
        Page<Leave> expectedLeaves = new PageImpl<>([new Leave(employee, LocalDate.of(2023, 8, 10), LocalDate.of(2023, 8, 10), "Vacation Leave"),
                                                     new Leave(employee, LocalDate.of(2023, 8, 11), LocalDate.of(2023, 8, 14), "Vacation Leave 2"),
                                                     new Leave(employee, LocalDate.of(2023, 8, 20), LocalDate.of(2023, 8, 23), "Vacation Leave 3"),
        ])

        when:
        leaveController.fetchAllLeaves(max, page)

        then:
        1 * leaveService.fetchLeaves(max, page) >> expectedLeaves
        1 * leaveService.fetchTotalLeavesCount()
    }

    def "fetchEmployeeLeaves should return leaves of given employee id"() {
        given:
        int max = 3
        int page = 1
        Long employeeId = 1L
        Employee employee = new Employee("Test Employee", RoleType.MEMBER, 30, Mock(Employee))
        employee.getId() >> employeeId
        Page<Leave> expectedLeaves = new PageImpl<>([new Leave(employee, LocalDate.of(2023, 8, 10), LocalDate.of(2023, 8, 10), "Vacation Leave"),
                                                     new Leave(employee, LocalDate.of(2023, 8, 11), LocalDate.of(2023, 8, 14), "Vacation Leave 2"),
                                                     new Leave(employee, LocalDate.of(2023, 8, 20), LocalDate.of(2023, 8, 23), "Vacation Leave 3"),
        ])

        when:
        leaveController.fetchEmployeeLeaves(max, page, employeeId)

        then:
        1 * leaveService.fetchLeavesByEmpId(max, page, employeeId) >> expectedLeaves
        1 * leaveService.fetchTotalLeavesOfEmployeeCount(employeeId)
    }

    def "fetchLeavesUnderManager should return leaves of employees with given manager id"() {
        given:
        int max = 3
        int page = 1
        Long managerId = 1L
        Employee manager = new Employee("Test Manager", RoleType.MANAGER, 30, Mock(Employee))
        manager.getId() >> managerId
        Employee employee = new Employee("Test Employee", RoleType.MEMBER, 30, manager)
        employee.getId() >> 2L
        Page<Leave> expectedLeaves = new PageImpl<>([new Leave(employee, LocalDate.of(2023, 8, 10), LocalDate.of(2023, 8, 10), "Vacation Leave"),
                                                     new Leave(employee, LocalDate.of(2023, 8, 11), LocalDate.of(2023, 8, 14), "Vacation Leave 2"),
                                                     new Leave(employee, LocalDate.of(2023, 8, 20), LocalDate.of(2023, 8, 23), "Vacation Leave 3"),
        ])

        when:
        leaveController.fetchLeavesUnderManager(max, page, managerId)

        then:
        1 * leaveService.fetchLeavesUnderManager(max, page, managerId) >> expectedLeaves
        1 * leaveService.fetchTotalEmployeeLeaveUnderManagerCount(managerId)
    }

    def "createLeave should create leave with correct values"() {
        given:
        Long employeeId = 1L
        Employee employee = Mock(Employee)

        LocalDate startDate = LocalDate.of(2023, 8, 10)
        LocalDate endDate = LocalDate.of(2023, 8, 12)
        String reason = "Vacation Leave"
        LeaveRequest leaveRequest = new LeaveRequest(employee_id: employeeId, startDate: startDate, endDate: endDate, reason: reason)
        Leave leave = new Leave(employee: employee, startDate: startDate, endDate: endDate, reason: reason, days: 2, leaveStatus: LeaveStatus.PENDING)

        leaveService.createLeave(leaveRequest) >> leave

        when:
        LeaveResponse actualLeave = leaveController.createLeave(leaveRequest)

        then:
        startDate == actualLeave.getStartDate()
        endDate == actualLeave.getEndDate()
        reason == actualLeave.getReason()
        2 == actualLeave.getDays()
        LeaveStatus.PENDING == actualLeave.getLeaveStatus()
    }

    def "ApproveLeave"() {
    }

    def "RejectLeave"() {
    }

    def "CancelLeave"() {
    }
}
