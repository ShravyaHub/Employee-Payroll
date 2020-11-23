package com.bridgelabz.employeepayroll;

import org.junit.Assert;
import org.junit.Test;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import static com.bridgelabz.employeepayroll.EmployeePayrollService.IOService.DATABASE_IO;

public class EmployeePayrollTest {

    @Test
    public void givenEmployeePayrollData_WhenRetrieved_ShouldMatchNumberOfEmployees() throws EmployeePayrollException {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        List<EmployeePayrollData> employeePayrollData = employeePayrollService.readEmployeePayrollData(DATABASE_IO);
        Assert.assertEquals(5, employeePayrollData.size());
    }

    @Test
    public void givenNewSalaryForEmployee_WhenUpdated_ShouldSyncWithDatabase() throws EmployeePayrollException {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        List<EmployeePayrollData> employeePayrollData = employeePayrollService.readEmployeePayrollData(DATABASE_IO);
        employeePayrollService.updateEmployeeSalary("Shravya", 3000000);
        boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDatabase("Shravya");
        Assert.assertTrue(result);
    }

    @Test
    public void givenEmployeePayrollData_ShouldNumberOfEmployeesWithinDateRange() throws EmployeePayrollException {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        LocalDate start = LocalDate.of(2020, 03, 01);
        LocalDate end = LocalDate.of(2020, 04, 30);
        List<EmployeePayrollData> employeePayrollData = employeePayrollService.readEmployeePayrollData(DATABASE_IO, start, end);
        Assert.assertEquals(2, employeePayrollData.size());
    }

    @Test
    public void givenEmployeePayrollData_ShouldReturnSumOfFemaleEmployeeSalaries() throws EmployeePayrollException {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        Assert.assertEquals(3600000, employeePayrollService.readEmployeePayrollData("SUM", "Female"));
    }

    @Test
    public void givenEmployeePayrollData_ShouldReturnSumOfMaleEmployeeSalaries() throws EmployeePayrollException {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        Assert.assertEquals(600000, employeePayrollService.readEmployeePayrollData("SUM", "Male"));
    }

    @Test
    public void givenEmployeePayrollData_ShouldReturnAverageOfFemaleEmployeeSalaries() throws EmployeePayrollException {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        Assert.assertEquals(1200000, employeePayrollService.readEmployeePayrollData("AVG", "Female"));
    }

    @Test
    public void givenEmployeePayrollData_ShouldReturnAverageOfMaleEmployeeSalaries() throws EmployeePayrollException {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        Assert.assertEquals(300000, employeePayrollService.readEmployeePayrollData("AVG", "Male"));
    }

    @Test
    public void givenEmployeePayrollData_ShouldReturnMinimumOfFemaleEmployeeSalaries() throws EmployeePayrollException {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        Assert.assertEquals(100000, employeePayrollService.readEmployeePayrollData("MIN", "Female"));
    }

    @Test
    public void givenEmployeePayrollData_ShouldReturnMinimumOfMaleEmployeeSalaries() throws EmployeePayrollException {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        Assert.assertEquals(100000, employeePayrollService.readEmployeePayrollData("MIN", "Male"));
    }

    @Test
    public void givenEmployeePayrollData_ShouldReturnMaximumOfFemaleEmployeeSalaries() throws EmployeePayrollException {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        Assert.assertEquals(3000000, employeePayrollService.readEmployeePayrollData("MAX", "Female"));
    }

    @Test
    public void givenEmployeePayrollData_ShouldReturnMaximumOfMaleEmployeeSalaries() throws EmployeePayrollException {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        Assert.assertEquals(500000, employeePayrollService.readEmployeePayrollData("MAX", "Male"));
    }

    @Test
    public void givenEmployeePayrollData_ShouldReturnNumberOfFemaleEmployeeSalaries() throws EmployeePayrollException {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        Assert.assertEquals(3, employeePayrollService.readEmployeePayrollData("COUNT", "Female"));
    }

    @Test
    public void givenEmployeePayrollData_ShouldReturnNumberOfMaleEmployeeSalaries() throws EmployeePayrollException {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        Assert.assertEquals(2, employeePayrollService.readEmployeePayrollData("COUNT", "Male"));
    }

    @Test
    public void givenNewEmployee_WhenAdded_ShouldSyncWithDatabase() throws EmployeePayrollException {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        employeePayrollService.readEmployeePayrollData(DATABASE_IO);
        employeePayrollService.addNewEmployee("Raj", 100000, LocalDate.now(), "Male", 3, 1);
        boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDatabase("Raj");
        Assert.assertTrue(result);
    }

    @Test
    public void givenEmployeeName_WhenRemoved_ShouldRemoveEmployeeFromList() throws EmployeePayrollException {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        employeePayrollService.readEmployeePayrollData(DATABASE_IO);
        employeePayrollService.deleteEmployee("Raj");
        boolean result = employeePayrollService.checkIfDeleted("Raj");
        Assert.assertTrue(result);
    }

    @Test
    public void given6Employees_WhenAdded_ShouldMatchNumberOfEntries() throws EmployeePayrollException {
        EmployeePayrollData[] arrayOfEmployees = {
                new EmployeePayrollData(0, "Jeff", 100, LocalDate.now(), "Male", 1, 1),
                new EmployeePayrollData(0, "Lasya", 200, LocalDate.now(), "Female", 1, 1),
                new EmployeePayrollData(0, "Mark", 300, LocalDate.now(), "Male", 1, 1),
                new EmployeePayrollData(0, "Cam", 120, LocalDate.now(), "Female", 1, 1),
                new EmployeePayrollData(0, "Noah", 400, LocalDate.now(), "Male", 1, 1),
                new EmployeePayrollData(0, "Mary", 600, LocalDate.now(), "Female", 1, 1),
        };

        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        employeePayrollService.readEmployeePayrollData(DATABASE_IO);
        Instant start = Instant.now();
        employeePayrollService.addEmployeesToPayrollList(Arrays.asList(arrayOfEmployees));
        Instant end = Instant.now();
        System.out.println("Duration without thread: " + Duration.between(start, end));
        Instant threadStart = Instant.now();
        employeePayrollService.addEmployeeToPayrollWithThreads(Arrays.asList(arrayOfEmployees));
        Instant threadEnd = Instant.now();
        System.out.println("Duration with thread: " + Duration.between(threadStart, threadEnd));
        Assert.assertEquals(66, new EmployeePayrollService().readEmployeePayrollData(DATABASE_IO).size());
    }

}
