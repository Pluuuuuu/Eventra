package com.eventra;

import com.zaxxer.hikari.*;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class Db {
    private static final HikariDataSource ds;
    static {
        try (InputStream in = Db.class.getResourceAsStream("/config.properties")) {
            Properties p = new Properties();
            p.load(in);
            HikariConfig cfg = new HikariConfig();
            cfg.setJdbcUrl(p.getProperty("db.url"));
            cfg.setUsername(p.getProperty("db.user"));
            cfg.setPassword(p.getProperty("db.password"));
            ds = new HikariDataSource(cfg);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize DB pool", e);
        }
    }

    /** Get a pooled connection */
    public static Connection get() throws SQLException {
        return ds.getConnection();
    }

    /** Clean up on app exit */
    public static void close() {
        ds.close();
    }
}
