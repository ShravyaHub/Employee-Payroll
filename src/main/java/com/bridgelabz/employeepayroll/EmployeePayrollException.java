package com.bridgelabz.employeepayroll;

public class EmployeePayrollException extends Exception {

    enum ExceptionType {
        CONNECTION_FAIL, CANNOT_EXECUTE_QUERY, UPDATE_FAILED, CANNOT_ADD_DATA_TO_SERVER;

    }

    ExceptionType exceptionType;

    public EmployeePayrollException(String message, ExceptionType exceptionType) {
        super(message);
        this.exceptionType = exceptionType;
    }

}
