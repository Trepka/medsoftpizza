package com.medsoft.pizza.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JdbcConnection {
    private static final Logger LOGGER = Logger.getLogger(JdbcConnection.class.getName());
    private static Optional<Connection> connection = Optional.empty();

    public static Optional<Connection> getConnection(){
        if (connection.isEmpty()) {
            String url = DbConnectionConfig.url;
            String username = DbConnectionConfig.username;
            String password = DbConnectionConfig.password;

            try {
                connection = Optional.ofNullable(DriverManager.getConnection(url, username, password));
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, null, e);
            }
        }
        return connection;
    }
}
