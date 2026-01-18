package com.gestion.productos.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DB {

    public static Connection getConnection() {
        Connection con = null;
        Properties props = new Properties();

        try {

            InputStream is = DB.class.getClassLoader().getResourceAsStream("database.properties");
            if (is == null) {
                throw new RuntimeException("No se encuentra el archivo database.properties");
            }
            props.load(is);

            con = DriverManager.getConnection(
                props.getProperty("db.url"),
                props.getProperty("db.user"),
                props.getProperty("db.password")
            );
            
            System.out.println("Conexión establecida correctamente.");

        } catch (IOException e) {
            System.out.println("Error al leer el archivo de configuración.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Error de SQL al conectar.");
            e.printStackTrace();
        }

        return con;
    }
}