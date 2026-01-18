package com.gestion.productos.dao;

import com.gestion.productos.model.Product;
import com.gestion.productos.util.DB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO de Product.
 * Se encarga del acceso a datos de la tabla PRODUCTS.
 * Esta clase no imprime por consola: devuelve objetos/listas para que Service/Main decidan qué mostrar.
*/

public class ProductDAO {

    /**
     * Busca un producto por su ID (campo ID_PRODUCT).
     *
     * @param id id del producto
     * @return Product si existe; null si no existe
    */

    public Product findById(int id) {
        
        String sql = "SELECT ID_PRODUCT, CLIENT_ID, NAME FROM PRODUCTS WHERE ID_PRODUCT = ?";

        try (Connection con = DB.getConnection();
            
            PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Product(
                        rs.getInt("ID_PRODUCT"),
                        rs.getInt("CLIENT_ID"),
                        rs.getString("NAME")
                    );
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error en ProductDAO.findById: " + e.getMessage(), e);
        }

        return null;
    }

    /**
     * Busca un producto por cliente + nombre.
     * Esto permite que el mismo nombre de producto exista en clientes distintos.
     *
     * @param clientId id del cliente (CLIENT_ID)
     * @param name     nombre del producto
     * @return Product si existe; null si no existe
    */

    public Product finByClientAndName(int clientId, String name) {
        
        String sql = "SELECT ID_PRODUCT, CLIENT_ID, NAME FROM PRODUCTS WHERE CLIENT_ID = ? AND NAME = ?";

        try (Connection con = DB.getConnection();
            
            PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, clientId);
            stmt.setString(2, name);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Product(
                        rs.getInt("ID_PRODUCT"),
                        rs.getInt("CLIENT_ID"),
                        rs.getString("NAME")
                    );
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error en ProductDAO.finByClientAndName: " + e.getMessage(), e);
        }

        return null;
    }

    /**
     * Devuelve todos los productos de un cliente concreto.
     *
     * @param clientId id del cliente (CLIENT_ID)
     * @return lista de productos del cliente; si no hay, lista vacía
    */

    public List<Product> finByClientId(int clientId) {
        
        String sql = "SELECT ID_PRODUCT, CLIENT_ID, NAME FROM PRODUCTS WHERE CLIENT_ID = ? ORDER BY ID_PRODUCT";
        List<Product> products = new ArrayList<>();

        try (Connection con = DB.getConnection();
            
            PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, clientId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    products.add(new Product(
                        rs.getInt("ID_PRODUCT"),
                        rs.getInt("CLIENT_ID"),
                        rs.getString("NAME")
                    ));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error en ProductDAO.finByClientId: " + e.getMessage(), e);
        }

        return products;
    }

    /**
     * Devuelve todos los productos del sistema.
     * Yo lo uso para mostrarlos por consola cuando quiero elegir productos sin abrir la base de datos.
     *
     * @return lista de productos; si no hay, lista vacía
    */

    public List<Product> findAll() {

        String sql = "SELECT ID_PRODUCT, CLIENT_ID, NAME FROM PRODUCTS ORDER BY CLIENT_ID, ID_PRODUCT";
        List<Product> products = new ArrayList<>();

        try (Connection con = DB.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                products.add(new Product(
                    rs.getInt("ID_PRODUCT"),
                    rs.getInt("CLIENT_ID"),
                    rs.getString("NAME")
                ));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error en ProductDAO.findAll: " + e.getMessage(), e);
        }

        return products;
    }

    //Buscará posibles productos similares en otros clientes.
    public List<Product> findSameNameInOtherClients(int clientId, String name) {

        String sql =
            "SELECT ID_PRODUCT, CLIENT_ID, NAME " +
            "FROM PRODUCTS " +
            "WHERE NAME = ? AND CLIENT_ID <> ? " +
            "ORDER BY CLIENT_ID, ID_PRODUCT";

        List<Product> products = new ArrayList<>();

        try (Connection con = DB.getConnection();
        PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setInt(2, clientId);

        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                products.add(new Product(
                    rs.getInt("ID_PRODUCT"),
                    rs.getInt("CLIENT_ID"),
                    rs.getString("NAME")
                ));
            }
        }

    } catch (SQLException e) {
        throw new RuntimeException("Error en ProductDAO.findSameNameInOtherClients: " + e.getMessage(), e);
    }
        return products;
    }


    /**
     * Inserta un producto para un cliente y devuelve el ID generado.
     *
     * @param idCliente id del cliente (CLIENT_ID)
     * @param name      nombre del producto
     * @return id generado si se inserta; -1 si no se insertó ninguna fila
    */

    public int insertProduct(int idCliente, String name) {
        
        String sql = "INSERT INTO PRODUCTS (CLIENT_ID, NAME) VALUES (?, ?)";
        int generatedId = -1;

        try (Connection con = DB.getConnection();
            
            PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, idCliente);
            stmt.setString(2, name);

            int rows = stmt.executeUpdate();
            if (rows == 0) return -1;
            
            // Cojo el ID autogenerado para poder devolverlo y mostrarlo en la consola si hace falta
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    generatedId = keys.getInt(1);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error en ProductDAO.insertProduct: " + e.getMessage(), e);
        }

        return generatedId;
    }
}