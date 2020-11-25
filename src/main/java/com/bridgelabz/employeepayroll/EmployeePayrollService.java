package com.bridgelabz.employeepayroll;

import io.restassured.*;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONException;
import org.json.JSONObject;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeePayrollService {

    public void addEmployeeToPayrollWithThreads(List<EmployeePayrollData> employeePayrollDataList) throws EmployeePayrollException {
        Map<Integer, Boolean> employeeMap = new HashMap<>();
        for(int index = 0; index < employeePayrollDataList.size(); index++) {
            int finalIndex = index;
            Runnable task = () -> {
                employeeMap.put(employeePayrollDataList.hashCode(), false);
                System.out.println("Employee being added: " + Thread.currentThread().getName());
                try {
                    this.addNewEmployee(employeePayrollDataList.get(finalIndex).name, employeePayrollDataList.get(finalIndex).salary, employeePayrollDataList.get(finalIndex).startDate, employeePayrollDataList.get(finalIndex).gender, employeePayrollDataList.get(finalIndex).department, employeePayrollDataList.get(finalIndex).is_active);
                } catch (EmployeePayrollException employeePayrollException) {
                    new EmployeePayrollException("Cannot execute query", EmployeePayrollException.ExceptionType.CANNOT_EXECUTE_QUERY);
                }
                employeeMap.put(employeePayrollDataList.hashCode(), true);
                System.out.println("Employee added: " + Thread.currentThread().getName());
            };
            Thread thread = new Thread(task, employeePayrollDataList.get(index).name);
            thread.start();
        }
        while (employeeMap.containsValue(false) && employeeMap.size() != 6) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException interruptedException) {
                throw new EmployeePayrollException(interruptedException.getMessage(), EmployeePayrollException.ExceptionType.CANNOT_EXECUTE_QUERY);
            }
        }
        System.out.println(employeePayrollData);
    }

    public void updateEmployeeDetailsWithThreads(List<EmployeePayrollData> employeePayrollDataList) throws EmployeePayrollException {
        Map<Integer, Boolean> employeeMap = new HashMap<>();
        for(int index = 0; index < employeePayrollDataList.size(); index++) {
            int finalIndex = index;
            Runnable task = () -> {
                employeeMap.put(employeePayrollDataList.hashCode(), false);
                System.out.println("Employee being updated: " + Thread.currentThread().getName());
                try {
                    this.updateEmployeeSalary(employeePayrollDataList.get(finalIndex).name, employeePayrollDataList.get(finalIndex).salary);
                } catch (EmployeePayrollException employeePayrollException) {
                    new EmployeePayrollException("Cannot execute query", EmployeePayrollException.ExceptionType.CANNOT_EXECUTE_QUERY);
                }
                employeeMap.put(employeePayrollDataList.hashCode(), true);
                System.out.println("Employee updated: " + Thread.currentThread().getName());
            };
            Thread thread = new Thread(task, employeePayrollDataList.get(index).name);
            thread.start();
        }
        while (employeeMap.containsValue(false) && employeeMap.size() != 6) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException interruptedException) {
                throw new EmployeePayrollException(interruptedException.getMessage(), EmployeePayrollException.ExceptionType.CANNOT_EXECUTE_QUERY);
            }
        }
        System.out.println(employeePayrollData);
    }

    public enum IOService {
        DATABASE_IO
    }

    private List<EmployeePayrollData> employeePayrollData;
    private EmployeePayrollDatabaseService employeePayrollDatabaseService;

    public EmployeePayrollService() {
        employeePayrollDatabaseService = EmployeePayrollDatabaseService.getInstance();
    }

    public void updateEmployeeSalary(String name, double salary) throws EmployeePayrollException {
        int result = EmployeePayrollDatabaseService.getInstance().updateEmployeeData(name, salary);
        if (result == 0) throw new EmployeePayrollException("Salary update failed", EmployeePayrollException.ExceptionType.UPDATE_FAILED);
        EmployeePayrollData employeePayrollData = this.getEmployeePayrollData(name);
        if (employeePayrollData != null) employeePayrollData.salary = salary;
    }

    private EmployeePayrollData getEmployeePayrollData(String name) {
        return this.employeePayrollData.stream()
                .filter(employeePayrollDataItem -> employeePayrollDataItem.name.equals(name))
                .findFirst()
                .orElse(null);
    }

    public boolean checkEmployeePayrollInSyncWithDatabase(String name) throws EmployeePayrollException {
        try {
            List<EmployeePayrollData> employeePayrollData = EmployeePayrollDatabaseService.getInstance().getEmployeePayrollData(name);
            return employeePayrollData.get(0).equals(getEmployeePayrollData(name));
        } catch (EmployeePayrollException employeePayrollException) {
            throw new EmployeePayrollException("Cannot execute query", EmployeePayrollException.ExceptionType.CANNOT_EXECUTE_QUERY);
        }
    }

    public List<EmployeePayrollData> readEmployeePayrollData(IOService ioService) throws EmployeePayrollException {
        try {
            if (ioService.equals(IOService.DATABASE_IO))
                return this.employeePayrollData = EmployeePayrollDatabaseService.getInstance().readData();
            return this.employeePayrollData;
        } catch (EmployeePayrollException employeePayrollException) {
            throw new EmployeePayrollException("Cannot execute query", EmployeePayrollException.ExceptionType.CANNOT_EXECUTE_QUERY);
        }
    }

    public int readEmployeePayrollData(String action, String gender) throws EmployeePayrollException {
        try {
            return EmployeePayrollDatabaseService.getInstance().readData(action, gender);
        } catch (EmployeePayrollException employeePayrollException) {
            throw new EmployeePayrollException("Cannot execute query", EmployeePayrollException.ExceptionType.CANNOT_EXECUTE_QUERY);
        }
    }

    public List<EmployeePayrollData> readEmployeePayrollData(IOService ioService, LocalDate start, LocalDate end) throws EmployeePayrollException {
        try {
            if (ioService.equals(IOService.DATABASE_IO))
                return employeePayrollDatabaseService.readData(start, end);
            return this.employeePayrollData;
        } catch (EmployeePayrollException employeePayrollException) {
            throw new EmployeePayrollException("Cannot execute query", EmployeePayrollException.ExceptionType.CANNOT_EXECUTE_QUERY);
        }
    }

    public void addNewEmployee(String name, double salary, LocalDate startDate, String gender, int department, int active) throws EmployeePayrollException {
        employeePayrollData.add(employeePayrollDatabaseService.addNewEmployee(name, salary, startDate, gender, department, active));
    }

    public void deleteEmployee(String name) throws EmployeePayrollException {
        this.employeePayrollData = this.employeePayrollDatabaseService.deleteEmployee(name);
    }

    public boolean checkIfDeleted(String name) throws EmployeePayrollException {
        List<EmployeePayrollData> employeePayrollData = employeePayrollDatabaseService.getEmployeePayrollData(name);
        return getEmployeePayrollData(name) == null && employeePayrollData.size() == 0;
    }

    public void addEmployeesToPayrollList(List<EmployeePayrollData> employeePayrollList) throws EmployeePayrollException {
        for(int i = 0; i < employeePayrollList.size(); i++) {
            System.out.println("Employee being added: " + employeePayrollData.get(i).name);
            this.addNewEmployee(employeePayrollList.get(i).name, employeePayrollList.get(i).salary, employeePayrollList.get(i).startDate, employeePayrollList.get(i).gender, employeePayrollList.get(i).department, employeePayrollList.get(i).is_active);
            System.out.println("Employee added: " + employeePayrollData.get(i).name);
        }
    }

    public int addEmployeeToJSONServer(int id, String name, double salary) throws JSONException {
        RestAssured.baseURI ="http://localhost:3000";
        RequestSpecification request = RestAssured.given();
        JSONObject requestParams = new JSONObject();
        requestParams.put("id", id);
        requestParams.put("name", name);
        requestParams.put("salary", salary);
        request.header("Content-Type", "application/json");
        request.body(requestParams.toString());
        Response response = request.post("/employees");
        return response.getStatusCode();
    }

}
