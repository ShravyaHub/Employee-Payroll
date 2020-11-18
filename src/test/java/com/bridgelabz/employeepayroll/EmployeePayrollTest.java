package com.bridgelabz.employeepayroll;

import org.junit.Assert;
import org.junit.Test;
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
        List<EmployeePayrollData> employeePayrollData = employeePayrollService.readEmployeePayrollData(DATABASE_IO, "2020-03-01", "2020-04-30");
        Assert.assertEquals(2, employeePayrollData.size());
    }

}
