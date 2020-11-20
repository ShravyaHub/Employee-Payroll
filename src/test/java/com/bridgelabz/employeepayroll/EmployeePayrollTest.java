package com.bridgelabz.employeepayroll;

import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
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
        employeePayrollService.addNewEmployee("Raj", 100000, LocalDate.now(), "Male", "Sales");
        boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDatabase("Raj");
        Assert.assertTrue(result);
    }

}
