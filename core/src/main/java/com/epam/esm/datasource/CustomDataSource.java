package com.epam.esm.datasource;

import com.epam.esm.exception.ClosingConnectionException;
import com.epam.esm.exception.CustomDataSourceException;
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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CustomDataSource extends AbstractDataSource implements SmartDataSource {
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

    private BlockingQueue<Connection> pool;
    private AtomicInteger connectionCount;
    private Lock lock;

    private CustomDataSource() {
    }

    @PostConstruct
    private void init() {
        try {
            pool = new ArrayBlockingQueue<>(poolCapacity);
            connectionCount = new AtomicInteger();
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
    public Connection getConnection() throws CustomDataSourceException {
        Connection connection;
        try {
            connection = pool.poll(timeoutSeconds, TimeUnit.SECONDS);
            lock.lock();
            if (connection == null && connectionCount.get() < poolCapacity) {
                connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
                pool.offer(connection);
                connectionCount.incrementAndGet();
            }

            if (connection == null) {
                throw new CustomDataSourceException("Too many connections.");
            }
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
            if (!connection.isClosed()) {
                pool.offer(connection);
                return false;
            } else {
                pool.remove(connection);
                return true;
            }
        } catch (SQLException e) {
            throw new ClosingConnectionException(e);
        }
    }

    @PreDestroy
    private void close() {
        pool.forEach(this::closeConnection);
    }

    private void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new ClosingConnectionException(e);
            }
        }
    }

    private static class CustomDataSourceHolder {
        private static final CustomDataSource INSTANCE = new CustomDataSource();
    }
}
