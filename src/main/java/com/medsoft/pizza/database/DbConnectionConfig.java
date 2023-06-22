package com.medsoft.pizza.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DbConnectionConfig {
    public static String driver;
    public static String url;
    public static String username;
    public static String password;
    @Autowired
    public DbConnectionConfig getDbConnectionConfig(@Value("${development.driver}") String driver,
                                          @Value("${development.url}") String url,
                                          @Value("${development.username}") String username,
                                          @Value("${development.password}") String password) {
        DbConnectionConfig.driver = driver;
        DbConnectionConfig.url = url;
        DbConnectionConfig.username = username;
        DbConnectionConfig.password = password;
        return this;
    }
}
