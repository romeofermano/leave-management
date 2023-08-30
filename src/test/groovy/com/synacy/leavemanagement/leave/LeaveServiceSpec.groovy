package com.synacy.leavemanagement.leave

import com.synacy.leavemanagement.employee.Employee
import com.synacy.leavemanagement.employee.EmployeeRepository
import com.synacy.leavemanagement.employee.EmployeeService
import com.synacy.leavemanagement.enums.LeaveStatus
import com.synacy.leavemanagement.enums.RoleType
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import spock.lang.Specification

import java.time.LocalDate
import java.util.stream.Collectors

class LeaveServiceSpec extends Specification {
    LeaveService leaveService
    LeaveRepository leaveRepository
    EmployeeService employeeService
    EmployeeRepository employeeRepository

    void setup() {
        leaveRepository = Mock(LeaveRepository)
        employeeRepository = Mock(EmployeeRepository)
        employeeService = Mock(EmployeeService)
        leaveService = new LeaveService(leaveRepository, employeeService, employeeRepository)
    }

    def "fetchLeaveId should respond with correct leave values given the id"() {
        given:
        Long leaveId = 1L
        Leave leave = new Leave(Mock(Employee), LocalDate.of(2023, 8, 10), LocalDate.of(2023, 8, 12), "Vacation Leave")
        leave.setLeaveStatus(LeaveStatus.PENDING)

        when:
        Optional<Leave> actualLeave = leaveService.fetchLeaveId(leaveId)

        then:
        1 * leaveRepository.findById(leaveId) >> Optional.of(leave)
        leave.getStartDate() == actualLeave.get().getStartDate()
        leave.getEndDate() == actualLeave.get().getEndDate()
        2 == actualLeave.get().getDays()
        leave.getReason() == actualLeave.get().getReason()
        LeaveStatus.PENDING == actualLeave.get().getLeaveStatus()
    }

    def "fetchLeaves should respond with all leaves"() {
        given:
        Page<Leave> expectedLeaves = new PageImpl<>([new Leave(Mock(Employee), LocalDate.of(2023, 8, 10), LocalDate.of(2023, 8, 10), "Vacation Leave"),
                                                     new Leave(Mock(Employee), LocalDate.of(2023, 8, 11), LocalDate.of(2023, 8, 14), "Vacation Leave 2"),
                                                     new Leave(Mock(Employee), LocalDate.of(2023, 8, 20), LocalDate.of(2023, 8, 23), "Vacation Leave 3"),
        ])

        when:
        Page<Leave> actualLeaves = leaveService.fetchLeaves(3, 1)

        then:
        1 * leaveRepository.findAllExceptHRAdmin(RoleType.HR_ADMIN, _) >> expectedLeaves
        expectedLeaves == actualLeaves
    }

    def "fetchLeavesByEmpId should respond with leaves of employee given employeeId"() {
        given:
        Long employeeId = 1L
        Employee employee = new Employee("Test Employee", RoleType.MEMBER, 30, Mock(Employee))
        employee.setId(employeeId)
        Page<Leave> expectedLeaves = new PageImpl<>([new Leave(employee, LocalDate.of(2023, 8, 10), LocalDate.of(2023, 8, 10), "Vacation Leave"),
                                                     new Leave(employee, LocalDate.of(2023, 8, 11), LocalDate.of(2023, 8, 14), "Vacation Leave 2"),
                                                     new Leave(employee, LocalDate.of(2023, 8, 20), LocalDate.of(2023, 8, 23), "Vacation Leave 3"),
        ])
        List<Long> employeeIds = expectedLeaves.getContent().stream().map { leave -> leave.getEmployee().getId() }.collect(Collectors.toList())

        when:
        Page<Leave> actualLeave = leaveService.fetchLeavesByEmpId(2, 1, employeeId)

        then:
        1 * leaveRepository.findAllByEmployee_Id(employeeId, _) >> expectedLeaves
        [1L, 1L, 1L] == employeeIds
        expectedLeaves == actualLeave
    }

    def "fetchLeavesUnderManager should respond with leave of employees under given manager id"() {
        given:
        Long managerId = 1L
        Employee manager = new Employee("Test Manager", RoleType.MANAGER, 30, Mock(Employee))
        manager.getId() >> managerId
        Employee employee = new Employee("Test Employee", RoleType.MEMBER, 30, manager)
        employee.getId() >> 2L
        Page<Leave> expectedLeaves = new PageImpl<>([new Leave(employee, LocalDate.of(2023, 8, 10), LocalDate.of(2023, 8, 10), "Vacation Leave"),
                                                     new Leave(employee, LocalDate.of(2023, 8, 11), LocalDate.of(2023, 8, 14), "Vacation Leave 2"),
                                                     new Leave(employee, LocalDate.of(2023, 8, 20), LocalDate.of(2023, 8, 23), "Vacation Leave 3"),
        ])
        List<Long> managerIds = expectedLeaves.getContent().stream().map { leave -> leave.getEmployee().getManager().getId() }.collect(Collectors.toList())

        when:
        Page<Leave> actualLeave = leaveService.fetchLeavesByEmpId(2, 1, managerId)

        then:
        1 * leaveRepository.findAllByEmployee_Id(managerId, _) >> expectedLeaves
//        [1L, 1L, 1L] == managerIds
        expectedLeaves == actualLeave
    }

    def "fetchPendingLeave should respond with leave of status pending given leave id"() {
        given:
        Long leaveId = 1L
        Leave leave = new Leave(Mock(Employee), LocalDate.of(2023, 8, 10), LocalDate.of(2023, 8, 12), "Vacation Leave")
        leave.setLeaveStatus(LeaveStatus.PENDING)

        when:
        Optional<Leave> actualLeave = leaveService.fetchPendingLeave(leaveId)

        then:
        1 * leaveRepository.findByIdAndLeaveStatus(leaveId, LeaveStatus.PENDING) >> leave
        leave.getStartDate() == actualLeave.get().getStartDate()
        leave.getEndDate() == actualLeave.get().getEndDate()
        2 == actualLeave.get().getDays()
        leave.getReason() == actualLeave.get().getReason()
        LeaveStatus.PENDING == actualLeave.get().getLeaveStatus()
    }

    def "createLeave should create leave with correct values"() {
        given:
        Long employeeId = 1L
        Employee employee = Mock(Employee)

        LocalDate startDate = LocalDate.of(2023, 8, 10)
        LocalDate endDate = LocalDate.of(2023, 8, 12)
        String reason = "Vacation Leave"
        LeaveRequest leaveRequest = new LeaveRequest(employee_id: employeeId, startDate: startDate, endDate: endDate, reason: reason)

        employeeService.fetchEmployeeById(employeeId) >> employee

        when:
        leaveService.createLeave(leaveRequest)

        then:
        1 * leaveRepository.save(*_) >> {
            Leave leave ->
                assert employee == leave.getEmployee()
                assert startDate == leave.getStartDate()
                assert endDate == leave.getEndDate()
                assert 2 == leave.getDays()
                assert reason == leave.getReason()
                assert LeaveStatus.PENDING == leave.getLeaveStatus()
        }
    }

    def "approveLeave should change leave status to approved given leave"() {
        given:
        Leave leave = new Leave(Mock(Employee), LocalDate.of(2023, 8, 10), LocalDate.of(2023, 8, 12), "Vacation Leave")
        leave.setLeaveStatus(LeaveStatus.PENDING)

        when:
        Leave actualLeave = leaveService.approveLeave(leave)

        then:
        1 * leaveRepository.save(leave) >> leave
        LeaveStatus.APPROVED == actualLeave.leaveStatus
    }

    def "rejectLeave should change leave status to rejected given leave"() {
        given:
        Long employeeId = 1L
        Employee employee = new Employee("Test Employee", RoleType.MEMBER, 30, Mock(Employee))
        employee.setId(1L)
        Leave leave = new Leave(employee, LocalDate.of(2023, 8, 10), LocalDate.of(2023, 8, 12), "Vacation Leave")
        leave.setLeaveStatus(LeaveStatus.PENDING)
        employeeService.fetchEmployeeById(employeeId) >> employee

        when:
        Leave actualLeave = leaveService.rejectLeave(leave)

        then:
        1 * leaveRepository.save(leave) >> leave
        LeaveStatus.REJECTED == actualLeave.leaveStatus
    }

    def "cancelLeave should change of leave status of pending leave to cancelled "() {
        given:
        Long employeeId = 1L
        Employee employee = new Employee("Test Employee", RoleType.MEMBER, 30, Mock(Employee))
        employee.setId(1L)
        Leave leave = new Leave(employee, LocalDate.of(2023, 8, 10), LocalDate.of(2023, 8, 12), "Vacation Leave")
        leave.setLeaveStatus(LeaveStatus.PENDING)
        employeeService.fetchEmployeeById(employeeId) >> employee

        when:
        leaveService.cancelLeave(leave)

        then:
        1 * leaveRepository.save(leave) >> leave
        LeaveStatus.CANCELLED == leave.leaveStatus
    }

    def "fetchTotalLeavesCount should return count of leaves"() {
        given:
        int expectedCount = 5

        leaveRepository.countAllBy() >> expectedCount

        when:
        int result = leaveService.fetchTotalLeavesCount()
        then:
        expectedCount == result
    }

    def "fetchTotalLeavesOfEmployeeCount should return count of leaves of an employee given employee id"() {
        given:
        Long employeeId = 1L
        int count = 5

        leaveRepository.countAllByEmployee_Id(employeeId) >> count

        when:
        int result = leaveService.fetchTotalLeavesOfEmployeeCount(employeeId)

        then:
        count == result
    }

    def "FetchTotalEmployeeLeaveUnderManagerCount should return count of leaves of employee with given manager if"() {
        given:
        Long managerId = 1L
        int count = 5

        leaveRepository.countAllByEmployee_Id(managerId) >> count

        when:
        int result = leaveService.fetchTotalLeavesOfEmployeeCount(managerId)

        then:
        count == result
    }
}
