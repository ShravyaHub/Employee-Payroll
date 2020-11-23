package com.bridgelabz.employeepayroll;

import java.time.LocalDate;
import java.util.Objects;

public class EmployeePayrollData {

    public int id;
    public String name;
    public double salary;
    public LocalDate startDate;
    public int department;
    public String gender;
    public int is_active;

    public EmployeePayrollData(Integer id, String name, Double salary) {
        this.id = id;
        this.name = name;
        this.salary = salary;
    }

    public EmployeePayrollData(int id, String name, double salary, LocalDate startDate, int department) {
        this(id, name, salary);
        this.startDate = startDate;
        this.department = department;
    }

    public EmployeePayrollData(int id, String name, double salary, LocalDate startDate, String gender, int department, int is_active) {
        this(id, name, salary, startDate, department);
        this.gender = gender;
        this.is_active = is_active;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, salary, startDate, gender, department, is_active);
    }

    @Override
    public String toString() {
        return "ID = " + id + ", Name = " + name + ", Salary = " + salary;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        EmployeePayrollData employeePayrollData = (EmployeePayrollData) object;
        return id == employeePayrollData.id && Double.compare(employeePayrollData.salary, salary) == 0 && name.equals(employeePayrollData.name);
    }
}
