package com.godrejjersey;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL =
            "jdbc:mysql://localhost:3306/godrej_jersey"
            + "?useSSL=false&serverTimezone=Asia/Kolkata";

    private static final String USER = "root";

    private static final String PASSWORD = "77840608";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}