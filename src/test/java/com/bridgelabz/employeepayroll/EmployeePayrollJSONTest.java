package com.bridgelabz.employeepayroll;

import org.json.JSONException;
import org.junit.Assert;
import org.junit.Test;

public class EmployeePayrollJSONTest {

    @Test
    public void givenEmployeePayrollData_WhenAddedToJSONServer_ShouldValidateRequestResponseReceived() throws JSONException {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        Assert.assertEquals(201, employeePayrollService.addEmployeeToJSONServer(3, "Priya", 10000));
    }

}
