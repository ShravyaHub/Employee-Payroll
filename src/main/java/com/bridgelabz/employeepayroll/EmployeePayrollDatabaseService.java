package com.bridgelabz.employeepayroll;

import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmployeePayrollDatabaseService {

    private PreparedStatement employeePayrollPreparedStatement;
    private static EmployeePayrollDatabaseService employeePayrollDatabaseService;
    public static EmployeePayrollDatabaseService getInstance() {
        if(employeePayrollDatabaseService == null) employeePayrollDatabaseService = new EmployeePayrollDatabaseService();
        return employeePayrollDatabaseService;
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

    public List<EmployeePayrollData> readData() throws EmployeePayrollException {
        String sql = "SELECT * FROM EmployeePayroll";
        return getEmployeePayrollDataUsingDatabase(sql);
    }

    public List<EmployeePayrollData> readData(LocalDate start, LocalDate end) throws EmployeePayrollException {
        String sql = String.format("SELECT * FROM EmployeePayroll WHERE StartDate BETWEEN '%s' AND '%s';", Date.valueOf(start), Date.valueOf(end));
        return getEmployeePayrollDataUsingDatabase(sql);
    }

    private List<EmployeePayrollData> getEmployeePayrollDataUsingDatabase(String sql) throws EmployeePayrollException {
        List<EmployeePayrollData> employeePayrollData = new ArrayList();
        try(Connection connection= this.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            employeePayrollData = this.getEmployeePayrollData(resultSet);
        } catch (SQLException sqlException) {
            throw new EmployeePayrollException("Cannot connect to database", EmployeePayrollException.ExceptionType.CONNECTION_FAIL);
        }
        return employeePayrollData;
    }

    public int readData(String calculate, String gender) throws EmployeePayrollException {
        int result;
        String sql = String.format("SELECT %s(Salary) FROM EmployeePayroll WHERE GENDER = '%s' GROUP BY Gender;", calculate, gender);
        try(Connection connection= this.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            resultSet.next();
            result = resultSet.getInt(1);
        } catch (SQLException sqlException) {
            throw new EmployeePayrollException("Cannot connect to database", EmployeePayrollException.ExceptionType.CONNECTION_FAIL);
        }
        return result;
    }

    public int updateEmployeeData(String name, double salary) throws EmployeePayrollException {
        try(Connection connection= this.getConnection()) {
            String sql = String.format("UPDATE EmployeePayroll SET Salary = %.2f WHERE Name = '%s';", salary, name);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            return preparedStatement.executeUpdate(sql);
        } catch (SQLException sqlException) {
            throw new EmployeePayrollException("Cannot connect to database", EmployeePayrollException.ExceptionType.CONNECTION_FAIL);
        }
    }

    public List<EmployeePayrollData> getEmployeePayrollData(String name) throws EmployeePayrollException {
        List<EmployeePayrollData> employeePayrollData;
        if(this.employeePayrollPreparedStatement == null) this.prepareStatementForEmployeeData();
        try {
            employeePayrollPreparedStatement.setString(1, name);
            ResultSet resultSet = employeePayrollPreparedStatement.executeQuery();
            employeePayrollData = this.getEmployeePayrollData(resultSet);
        } catch(SQLException sqlException) {
            throw new EmployeePayrollException("Cannot connect to database", EmployeePayrollException.ExceptionType.CONNECTION_FAIL);
        }
        return employeePayrollData;
    }

    private List<EmployeePayrollData> getEmployeePayrollData(ResultSet resultSet) throws EmployeePayrollException {
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
            throw new EmployeePayrollException("Cannot execute query", EmployeePayrollException.ExceptionType.CANNOT_EXECUTE_QUERY);
        }
        return employeePayrollData;
    }

    private void prepareStatementForEmployeeData() throws EmployeePayrollException {
        try {
            Connection connection = this.getConnection();
            String sql = "SELECT * FROM EmployeePayroll WHERE Name = ?";
            employeePayrollPreparedStatement = connection.prepareStatement(sql);
        } catch (SQLException sqlException) {
            throw new EmployeePayrollException("Cannot execute query", EmployeePayrollException.ExceptionType.CANNOT_EXECUTE_QUERY);
        }
    }

    public EmployeePayrollData addNewEmployee(String name, double salary, LocalDate startDate, String gender) throws EmployeePayrollException {
        int employeeID = -1;
        EmployeePayrollData employeePayrollData = null;
        String sql = String.format("INSERT INTO EmployeePayroll(Name, Salary, StartDate, Gender) VALUES ('%s', '%s', '%s', '%s')", name, salary, Date.valueOf(startDate), gender);
        try(Connection connection = this.getConnection()) {
            Statement statement = connection.createStatement();
            int rowAffected = statement.executeUpdate(sql, statement.RETURN_GENERATED_KEYS);
            if(rowAffected == 1) {
                ResultSet resultSet = statement.getGeneratedKeys();
                if(resultSet.next()) employeeID = resultSet.getInt(1);
            }
            employeePayrollData = new EmployeePayrollData(employeeID, name, salary, startDate);
        } catch (SQLException sqlException) {
            throw new EmployeePayrollException(sqlException.getMessage(), EmployeePayrollException.ExceptionType.CANNOT_EXECUTE_QUERY);
        }
        return employeePayrollData;
    }

}
