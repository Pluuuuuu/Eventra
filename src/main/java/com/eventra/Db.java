package com.eventra;

import com.zaxxer.hikari.*;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class Db {
    private static HikariDataSource ds;
    static {
        try (InputStream in = Db.class.getResourceAsStream("/config.properties")) {
            Properties p = new Properties();
            p.load(in);
            HikariConfig cfg = new HikariConfig();
            cfg.setJdbcUrl(p.getProperty("db.url"));
            cfg.setUsername(p.getProperty("db.user"));
            cfg.setPassword(p.getProperty("db.password"));
            cfg.setConnectionTimeout(30000); // 30 seconds
            cfg.setMaximumPoolSize(10);
            cfg.setMinimumIdle(5);
            ds = new HikariDataSource(cfg);
            System.out.println("Database connection pool initialized successfully");
        } catch (Exception e) {
            System.err.println("Warning: Failed to initialize DB pool. Database features will be disabled.");
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            ds = null;
        }
    }

    /** Get a pooled connection */
    public static Connection get() throws SQLException {
        if (ds == null) {
            throw new SQLException("Database connection pool not initialized");
        }
        return ds.getConnection();
    }

    /** Clean up on app exit */
    public static void close() {
        if (ds != null) {
            ds.close();
        }
    }
}
