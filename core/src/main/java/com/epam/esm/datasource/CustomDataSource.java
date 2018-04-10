package com.epam.esm.datasource;

import com.epam.esm.exception.ClosingConnectionException;
import com.epam.esm.exception.CustomDataSourceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.AbstractDataSource;
import org.springframework.jdbc.datasource.SmartDataSource;
import org.springframework.util.ObjectUtils;

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

    private int poolCapacity = 10;
    private int timeoutSeconds = 5;
    private String driverClassName;
    private String url;
    private String username;
    private String password;

    private BlockingQueue<Connection> freeConnections;
    private BlockingQueue<Connection> usedConnections;
    private volatile int connectionCount;
    private Lock lock;

    private CustomDataSource() {
    }

    public static CustomDataSource getInstance() {
        return CustomDataSourceHolder.INSTANCE;
    }

    public int getPoolCapacity() {
        return poolCapacity;
    }

    public void setPoolCapacity(int poolCapacity) {
        this.poolCapacity = poolCapacity;
    }

    public int getTimeoutSeconds() {
        return timeoutSeconds;
    }

    public void setTimeoutSeconds(int timeoutSeconds) {
        this.timeoutSeconds = timeoutSeconds;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void init() {
        try {
            freeConnections = new ArrayBlockingQueue<>(poolCapacity);
            usedConnections = new ArrayBlockingQueue<>(poolCapacity);
            lock = new ReentrantLock();
            Class.forName(driverClassName);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Could not load JDBC driver class [" + driverClassName + "]", e);
        }
    }

    @Override
    public Connection getConnection() {
        Connection connection;
        try {
            connection = freeConnections.poll(timeoutSeconds, TimeUnit.SECONDS);
            lock.lock();
            if (connection == null && connectionCount < poolCapacity) {
                connection = DriverManager.getConnection(url, username, password);
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
        if (ObjectUtils.nullSafeEquals(username, this.username) && ObjectUtils.nullSafeEquals(password, this.password)) {
            return this.getConnection();
        } else {
            throw new CustomDataSourceException("This connection pool does not support custom username and password");
        }
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

    public void close() {
        LOGGER.info("Closing free pool connections");
        freeConnections.forEach(this::closeConnection);
        LOGGER.info("Closing connections that weren't returned to the pool");
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
