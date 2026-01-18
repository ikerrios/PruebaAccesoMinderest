package com.gestion.productos.util;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase para obtener la conexión a la base de datos.
 */
public class DB {

    private static final String URL =
        "jdbc:mysql://localhost:3306/MINDEREST?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASS = "root"; 

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}