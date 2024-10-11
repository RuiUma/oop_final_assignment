package com.umatech.DB;

import com.umatech.TomcatApplication.annotation.Component;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Component
public class DatabaseManager {
    private final String URL;
    private final String USER;
    private final String PASSWORD;
    private final ConnectionPool connectionPool;
    private final Logger logger = LogManager.getLogger(this.getClass());



    public DatabaseManager() throws SQLException {
        this.URL = "jdbc:mysql://localhost:3306/oop_final";
        this.USER = "root";
        this.PASSWORD = "1234";
        this.connectionPool = new ConnectionPool(URL,USER,PASSWORD,10, 20);
        logger.info("database manager initialised");
    }

    public Connection getConnection() throws SQLException {
        return connectionPool.getConnection();
    }
}