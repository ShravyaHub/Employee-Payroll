package com.bridgelabz.employeepayroll;

import io.restassured.RestAssured;
import org.json.JSONException;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class EmployeePayrollJSONTest {

    @Test
    public void givenEmployeePayrollData_WhenAddedToJSONServer_ShouldValidateRequestResponseReceived() throws JSONException {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        Assert.assertEquals(201, employeePayrollService.addEmployeeToJSONServer(3, "Priya", 10000));
    }

    @Test
    public void givenMultipleEmployeePayrollData_WhenAddedToJSONServer_ShouldValidateRequestResponseReceived() throws EmployeePayrollException {
        EmployeePayrollData[] arrayOfEmployees = {
                new EmployeePayrollData(5, "Jeff", 1000.00),
                new EmployeePayrollData( 6,"Lasya", 2000.00),
        };
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        List<Integer> statusCodes = employeePayrollService.addEmployeeToJSONServer(Arrays.asList(arrayOfEmployees));
        Assert.assertEquals("201", statusCodes.get(0).toString());
        Assert.assertEquals("201", statusCodes.get(1).toString());
    }

}
