package com.umatech.DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;

public class ConnectionPool {
    private final String url;
    private final String user;
    private final String password;
    private final int maxConnections;
    private final LinkedList<Connection> connectionPool = new LinkedList<>();

    public ConnectionPool(String url, String user, String password, int initialConnections, int maxConnections) throws SQLException {
        this.url = url;
        this.user = user;
        this.password = password;
        this.maxConnections = maxConnections;

        // 创建初始连接并放入连接池
        for (int i = 0; i < initialConnections; i++) {
            connectionPool.add(createNewConnection());
        }
    }

    private Connection createNewConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    public synchronized Connection getConnection() throws SQLException {
        if (connectionPool.isEmpty()) {
            if (connectionPool.size() < maxConnections) {
                connectionPool.add(createNewConnection());
            } else {
                throw new SQLException("Max connection size");
            }
        }
        return connectionPool.removeFirst();
    }

    public synchronized void releaseConnection(Connection connection) {
        if (connection != null) {
            connectionPool.addLast(connection);
        }
    }

    public synchronized void closeAllConnections() throws SQLException {
        while (!connectionPool.isEmpty()) {
            Connection connection = connectionPool.removeFirst();
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        }
    }




}
