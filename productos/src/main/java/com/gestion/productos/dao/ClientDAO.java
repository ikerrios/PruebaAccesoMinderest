package com.gestion.productos.dao;

import com.gestion.productos.model.Client;
import com.gestion.productos.util.DB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO de Client.
 * Aquí dejo todo lo relacionado con acceso a datos de la tabla CLIENTS.
 * Esta clase no imprime por consola: devuelve objetos para que Service/Main decidan qué mostrar.
*/
public class ClientDAO {

    private Client buildClient(ResultSet rs) throws SQLException {
        return new Client(
            rs.getInt("ID_CLIENT"),
            rs.getString("CODE"),
            rs.getString("NAME")
        );
    }

    /**
     * Busca un cliente por su código (campo CODE).
     *
     * @param code código del cliente (ej: C001)
     * @return Client si existe; null si no existe
    */
    public Client findByCodigo(String code) {
        
        String sql = "SELECT ID_CLIENT, CODE, NAME FROM CLIENTS WHERE CODE = ?";

        try (Connection con = DB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, code);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return buildClient(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error en ClientDAO.findByCodigo: " + e.getMessage(), e);
        }

        return null;
    }

    /**
     * Busca un cliente por su ID (campo ID_CLIENT).
     *
     * @param id id del cliente
     * @return Client si existe; null si no existe
    */
    public Client findById(int id) {
        
        String sql = "SELECT ID_CLIENT, CODE, NAME FROM CLIENTS WHERE ID_CLIENT = ?";

        try (Connection con = DB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return buildClient(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error en ClientDAO.findById: " + e.getMessage(), e);
        }

        return null;
    }

    /**
     * Devuelve todos los clientes ordenados por ID.
     * Útil para mostrar un listado en consola (por ejemplo antes de pedir inputs).
     *
     * @return lista de clientes (si no hay, devuelve lista vacía)
    */
    public List<Client> findAll() {
        
        String sql = "SELECT ID_CLIENT, CODE, NAME FROM CLIENTS ORDER BY ID_CLIENT";
        List<Client> clients = new ArrayList<>();

        try (Connection con = DB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                clients.add(buildClient(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error en ClientDAO.findAll: " + e.getMessage(), e);
        }

        return clients;
    }

    /**
     * Inserta un cliente en la tabla CLIENTS y devuelve el ID generado.
     *
     * @param code código del cliente
     * @param name nombre del cliente
     * @return id generado si se inserta; -1 si no se insertó ninguna fila
    */
    public int insertClient(String code, String name) {
        
        String sql = "INSERT INTO CLIENTS (CODE, NAME) VALUES (?, ?)";
        int generatedId = -1;

        try (Connection con = DB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, code);
            stmt.setString(2, name);

            int rows = stmt.executeUpdate();
            if (rows == 0) return -1;
            
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    generatedId = keys.getInt(1);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error en ClientDAO.insertClient: " + e.getMessage(), e);
        }

        return generatedId;
    }
}