package com.epam.esm.datasource;

import com.epam.esm.exception.ClosingConnectionException;
import com.epam.esm.exception.CustomDataSourceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.datasource.AbstractDataSource;
import org.springframework.jdbc.datasource.SmartDataSource;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CustomDataSource extends AbstractDataSource implements SmartDataSource {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomDataSource.class);

    @Value("${pool.capacity}")
    private int poolCapacity;

    @Value("${pool.timeout.seconds}")
    private int timeoutSeconds;

    @Value("${db.driver}")
    private String driverClassName;

    @Value("${db.url}")
    private String dbUrl;

    @Value("${db.username}")
    private String dbUsername;

    @Value("${db.password}")
    private String dbPassword;

    private BlockingQueue<Connection> freeConnections;
    private BlockingQueue<Connection> usedConnections;
    private int connectionCount;
    private Lock lock;

    private CustomDataSource() {
    }

    @PostConstruct
    private void init() {
        try {
            freeConnections = new ArrayBlockingQueue<>(poolCapacity);
            usedConnections = new ArrayBlockingQueue<>(poolCapacity);
            lock = new ReentrantLock();
            Class.forName(driverClassName);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Could not load JDBC driver class [" + driverClassName + "]", e);
        }
    }

    public static CustomDataSource getInstance() {
        return CustomDataSourceHolder.INSTANCE;
    }

    @Override
    public Connection getConnection() {
        Connection connection;
        try {
            connection = freeConnections.poll(timeoutSeconds, TimeUnit.SECONDS);
            lock.lock();
            if (connection == null && connectionCount < poolCapacity) {
                connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
            }

            if (connection == null) {
                throw new CustomDataSourceException("Too many connections.");
            }
            usedConnections.offer(connection);
            connectionCount++;
        } catch (InterruptedException | SQLException e) {
            throw new CustomDataSourceException(e);
        } finally {
            lock.unlock();
        }
        return connection;
    }

    @Override
    public Connection getConnection(String username, String password) {
        dbUsername = username;
        dbPassword = password;
        return getConnection();
    }

    @Override
    public boolean shouldClose(Connection connection) {
        try {
            if (usedConnections.contains(connection) && !connection.isClosed()) {
                usedConnections.remove(connection);
                freeConnections.offer(connection);
                return false;
            } else if (connection.isClosed() && usedConnections.contains(connection)){
                usedConnections.remove(connection);
                return true;
            } else {
                throw new CustomDataSourceException("Connection you've passed doesn't belong to this pool.");
            }
        } catch (SQLException e) {
            LOGGER.error("Error while closing connection", e);
            throw new ClosingConnectionException(e);
        }
    }

    @PreDestroy
    private void close() {
        LOGGER.info("Destroying connection freeConnections");
        freeConnections.forEach(this::closeConnection);
        usedConnections.forEach(this::closeConnection);
    }

    private void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                LOGGER.error("Couldn't close connection", e);
            }
        }
    }

    private static class CustomDataSourceHolder {
        private static final CustomDataSource INSTANCE = new CustomDataSource();

        private CustomDataSourceHolder() {}
    }
}
