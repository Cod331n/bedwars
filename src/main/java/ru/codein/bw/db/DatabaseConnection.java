package ru.codein.bw.db;

import lombok.experimental.UtilityClass;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@UtilityClass
public class DatabaseConnection {
    public static Connection getConnection() throws SQLException {
        String jdbcUrl = String.format("jdbc:mysql://%s:%s/%s?useUnicode=true&characterEncoding=UTF-8&rewriteBatchedStatements=true&useSSL=false&serverTimezone=UTC",
                System.getenv("DATABASE_HOST"),
                System.getenv("DATABASE_PORT"),
                System.getenv("DATABASE_NAME"));
        String username = System.getenv("DATABASE_USERNAME");
        String password = System.getenv("DATABASE_PASSWORD");

        return DriverManager.getConnection(jdbcUrl, username, password);
    }
}
