package com.bridgelabz.employeepayroll;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class EmployeePayrollService {

    public enum IOService {
        DATABASE_IO
    }

    private List<EmployeePayrollData> employeePayrollData;

    public List<EmployeePayrollData> readEmployeePayrollData(IOService ioService) {
        if(ioService.equals(IOService.DATABASE_IO))
            return this.employeePayrollData = new EmployeePayrollDatabaseService().readData(null, null);
        return this.employeePayrollData;
    }

}
