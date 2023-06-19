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
        if(connection.isEmpty()) {
            String url = "jdbc:postgresql://localhost:5433/medsoftpizza";
            String user = "postgres";
            String password = "postgres";

            try {
                connection = Optional.ofNullable(DriverManager.getConnection(url, user, password));
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, null, e);
            }
        }
        return connection;
    }
}
