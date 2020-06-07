package com.home;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Sergey on 29.01.2017.
 */
public class SQLite {
    public static Connection connection;
    public static Statement statement;
    public static ResultSet resultSet;

    public  static void Connect() throws ClassNotFoundException, SQLException{
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:Test.s3db");
        statement = connection.createStatement();
        System.out.println("Database is connected");
    }

    public static void CreateTable() throws ClassNotFoundException, SQLException{
        statement.execute("DELETE FROM 'TestIO'");
        statement.execute("CREATE TABLE if not exists 'TestIO' ('id' INTEGER primary key autoincrement," +
                "'operand1' DECIMAL, 'operand2' DECIMAL, 'expected' DECIMAL)");
        System.out.println("Table is exist");
    }

    public static void WriteDataInTable() throws SQLException{
        statement.execute("INSERT INTO 'TestIO' ('operand1', 'operand2', 'expected') VALUES (1.0, 1.0, 1.0)");
        statement.execute("INSERT INTO 'TestIO' ('operand1', 'operand2', 'expected') VALUES (1.0, 2.0, 0.5)");
        System.out.println("Data is written in table");
    }

    public static void ReadDataFromTable() throws SQLException{
        resultSet = statement.executeQuery("SELECT * FROM TestIO");
        while (resultSet.next()){
            System.out.println(resultSet.getInt("id"));
            System.out.println(resultSet.getDouble("operand1"));
            System.out.println(resultSet.getDouble("operand2"));
            System.out.println(resultSet.getDouble("expected") + "\n");
        }
        System.out.println("End of table");
    }
}
