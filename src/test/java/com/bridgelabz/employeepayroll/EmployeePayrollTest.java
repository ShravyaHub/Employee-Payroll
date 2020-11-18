package com.bridgelabz.employeepayroll;

import org.junit.Assert;
import org.junit.Test;
import java.util.List;
import static com.bridgelabz.employeepayroll.EmployeePayrollService.IOService.DATABASE_IO;

public class EmployeePayrollTest {

    @Test
    public void givenEmployeePayrollData_WhenRetrieved_ShouldMatchNumberOfEmployees() {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        List<EmployeePayrollData> employeePayrollData = employeePayrollService.readEmployeePayrollData(DATABASE_IO);
        Assert.assertEquals(5, employeePayrollData.size());
    }

    @Test
    public void givenNewSalaryForEmployee_WhenUpdated_ShouldSyncWithDatabase() {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        List<EmployeePayrollData> employeePayrollData = employeePayrollService.readEmployeePayrollData(DATABASE_IO);
        employeePayrollService.updateEmployeeSalary("Shravya", 3000000);
        boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDatabase("Shravya");
        Assert.assertTrue(result);
    }

}
