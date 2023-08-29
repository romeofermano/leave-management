package com.synacy.leavemanagement.leave

import com.synacy.leavemanagement.PageResponse
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
                                                     new Leave(manager, LocalDate.of(2023, 8, 11), LocalDate.of(2023, 8, 14), "Vacation Leave 2")
        ])

        when:
        PageResponse<LeaveResponse> actualLeave = leaveController.fetchAllLeaves(max, page)

        then:
        1 * leaveService.fetchLeaves(max, page) >> expectedLeaves
        1 * leaveService.fetchTotalLeavesCount() >> 2

        2 == actualLeave.getTotalCount()
        page == actualLeave.getPageNumber()

        expectedLeaves.getContent()[0].getEmployee().getName() == actualLeave.getContent()[0].employeeName
        expectedLeaves.getContent()[1].getEmployee().getName() == actualLeave.getContent()[1].employeeName

    }

    def "fetchEmployeeLeaves should return leaves of given employee id"() {
        given:
        int max = 2
        int page = 1
        Long employeeId = 1L
        Employee employee = new Employee("Test Employee", RoleType.MEMBER, 30, Mock(Employee))
        employee.getId() >> employeeId
        Page<Leave> expectedLeaves = new PageImpl<>([new Leave(employee, LocalDate.of(2023, 8, 10), LocalDate.of(2023, 8, 10), "Vacation Leave"),
                                                     new Leave(employee, LocalDate.of(2023, 8, 11), LocalDate.of(2023, 8, 14), "Vacation Leave 2"),
        ])

        when:
        PageResponse<LeaveResponse> actualLeave = leaveController.fetchEmployeeLeaves(max, page, employeeId)

        then:
        1 * leaveService.fetchLeavesByEmpId(max, page, employeeId) >> expectedLeaves
        1 * leaveService.fetchTotalLeavesOfEmployeeCount(employeeId) >> 2
        2 == actualLeave.getTotalCount()
        page == actualLeave.getPageNumber()
        expectedLeaves.getContent()[0].getEmployee().getName() == actualLeave.getContent()[0].getEmployeeName()
        expectedLeaves.getContent()[1].getEmployee().getName() == actualLeave.getContent()[1].getEmployeeName()
    }

    def "fetchLeavesUnderManager should return leaves of employees with given manager id"() {
        given:
        int max = 3
        int page = 1
        Long managerId = 1L
        Employee manager = new Employee("Test Manager", RoleType.MANAGER, 30, Mock(Employee))
        manager.getId() >> managerId
        Employee employee = new Employee("Test Employee", RoleType.MEMBER, 30, Mock(Employee))
        employee.getId() >> 2L
        Page<Leave> expectedLeaves = new PageImpl<>([new Leave(employee, LocalDate.of(2023, 8, 10), LocalDate.of(2023, 8, 10), "Vacation Leave"),
                                                     new Leave(employee, LocalDate.of(2023, 8, 11), LocalDate.of(2023, 8, 14), "Vacation Leave 2"),
        ])

        when:
        PageResponse<LeaveResponse> actualLeave = leaveController.fetchLeavesUnderManager(max, page, managerId)

        then:
        1 * leaveService.fetchLeavesUnderManager(max, page, managerId) >> expectedLeaves
        1 * leaveService.fetchTotalEmployeeLeaveUnderManagerCount(managerId) >> 2

        2 == actualLeave.getTotalCount()
        page == actualLeave.getPageNumber()

        expectedLeaves.getContent()[0].getEmployee().getName() == actualLeave.getContent()[0].getEmployeeName()
        expectedLeaves.getContent()[1].getEmployee().getName() == actualLeave.getContent()[1].getEmployeeName()

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

    def "approveLeave should change leave status to approved"() {
        given:
        Long leaveId = 1L
        Employee employee = Mock(Employee)

        LocalDate startDate = LocalDate.of(2023, 8, 10)
        LocalDate endDate = LocalDate.of(2023, 8, 12)
        String reason = "Vacation Leave"
        Leave leave = new Leave(employee: employee, startDate: startDate, endDate: endDate, reason: reason, days: 2, leaveStatus: LeaveStatus.PENDING)

        when:
        LeaveResponse actualLeave = leaveController.approveLeave(leaveId)

        then:
        1 * leaveService.approveLeave(leave)
        1 * leaveService.fetchLeaveId(leaveId) >> Optional.of(leave)
        leave.getLeaveStatus() == actualLeave.getLeaveStatus()
    }

    def "rejectLeave should change leave status to rejected"() {
        given:
        Long leaveId = 1L
        Employee employee = Mock(Employee)

        LocalDate startDate = LocalDate.of(2023, 8, 10)
        LocalDate endDate = LocalDate.of(2023, 8, 12)
        String reason = "Vacation Leave"
        Leave leave = new Leave(employee: employee, startDate: startDate, endDate: endDate, reason: reason, days: 2, leaveStatus: LeaveStatus.PENDING)

        when:
        LeaveResponse actualLeave = leaveController.rejectLeave(leaveId)

        then:
        1 * leaveService.fetchLeaveId(leaveId) >> Optional.of(leave)
        1 * leaveService.rejectLeave(leave)
        leave.getLeaveStatus() == actualLeave.getLeaveStatus()
    }

    def "cancelLeave should change leave status to cancelled"() {
        given:
        Long leaveId = 1L
        Employee employee = Mock(Employee)
        LocalDate startDate = LocalDate.of(2023, 8, 10)
        LocalDate endDate = LocalDate.of(2023, 8, 12)
        String reason = "Vacation Leave"

        Leave leave = new Leave(employee: employee, startDate: startDate, endDate: endDate, reason: reason, days: 2, leaveStatus: LeaveStatus.PENDING)

        when:
        leaveController.cancelLeave(leaveId)

        then:
        1 * leaveService.fetchPendingLeave(leaveId) >> Optional.of(leave)
        1 * leaveService.cancelLeave(leave)
    }
}
