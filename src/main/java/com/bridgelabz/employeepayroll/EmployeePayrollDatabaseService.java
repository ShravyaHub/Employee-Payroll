package com.bridgelabz.employeepayroll;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmployeePayrollDatabaseService {

    private PreparedStatement employeePayrollPreparedStatement;
    EmployeePayrollDatabaseService() {

    }

    private Connection getConnection() throws SQLException {
        String jdbcURL = "jdbc:mysql://localhost:3306/EmployeePayrollService?useSSL=false";
        String username = "root";
        String password = "shravya";
        Connection connection;
        System.out.println("Connecting to database: " + jdbcURL);
        connection = DriverManager.getConnection(jdbcURL, username, password);
        System.out.println("Connection is successful: " + connection);
        return connection;
    }

    public List<EmployeePayrollData> readData() {
        String sql = "SELECT * FROM EmployeePayroll";
        List<EmployeePayrollData> employeePayrollData = new ArrayList();
        try(Connection connection= this.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            employeePayrollData = this.getEmployeePayrollData(resultSet);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return employeePayrollData;
    }

    public int updateEmployeeData(String name, double salary) {
        return this.updateEmployeeDataUsingPreparedStatement(name, salary);
    }

    public int updateEmployeeDataUsingPreparedStatement(String name, double salary) {
        String sql = String.format("UPDATE EmployeePayroll SET Salary = %.2f WHERE Name = '%s';", salary, name);
        try(Connection connection= this.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            return preparedStatement.executeUpdate(sql);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return 0;
    }

    public List<EmployeePayrollData> getEmployeePayrollData(String name) {
        List<EmployeePayrollData> employeePayrollData = null;
        if(this.employeePayrollPreparedStatement == null) this.prepareStatementForEmployeeData();
        try {
            employeePayrollPreparedStatement.setString(1, name);
            ResultSet resultSet = employeePayrollPreparedStatement.executeQuery();
            employeePayrollData = this.getEmployeePayrollData(resultSet);
        } catch(SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return employeePayrollData;
    }

    private List<EmployeePayrollData> getEmployeePayrollData(ResultSet resultSet) {
        List<EmployeePayrollData> employeePayrollData = new ArrayList();
        try {
            while(resultSet.next()) {
                int id = resultSet.getInt("ID");
                String name = resultSet.getString("Name");
                double salary = resultSet.getDouble("Salary");
                LocalDate startDate = resultSet.getDate("StartDate").toLocalDate();
                employeePayrollData.add(new EmployeePayrollData(id, name, salary, startDate));
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return employeePayrollData;
    }

    private void prepareStatementForEmployeeData() {
        try {
            Connection connection = this.getConnection();
            String sql = "SELECT * FROM EmployeePayroll WHERE Name = ?";
            employeePayrollPreparedStatement = connection.prepareStatement(sql);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }
}
