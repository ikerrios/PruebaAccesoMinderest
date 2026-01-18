package com.gestion.productos.dao;

import com.gestion.productos.model.Product;
import com.gestion.productos.util.DB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO de Equivalence.
 * Gestiona la tabla EQUIVALENCES y las consultas necesarias para:
 * - comprobar si existe una equivalencia
 * - insertar una equivalencia
 * - listar los productos equivalentes a uno dado
*/
public class EquivalenceDAO {

    /**
     * Comprueba si ya existe una equivalencia exacta entre dos productos.
     * Importante: este método asume que la pareja se guarda ya normalizada (A < B).
     *
     * @param productA id de producto A
     * @param productB id de producto B
     * @return true si existe; false si no existe
    */

    public boolean existsEquivalence(int productA, int productB) {
        
        String sql = "SELECT 1 FROM EQUIVALENCES WHERE PRODUCT_ID_A = ? AND PRODUCT_ID_B = ? LIMIT 1";

        try (Connection con = DB.getConnection();
            
            PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, productA);
            stmt.setInt(2, productB);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error en EquivalenceDAO.existsEquivalence: " + e.getMessage(), e);
        }
    }

    /**
     * Inserta una equivalencia entre dos productos.
     * Yo la llamo desde el Service después de normalizar el orden (min/max) para evitar duplicados invertidos.
     *
     * @param productA id de producto A
     * @param productB id de producto B
     * @return true si se insertó; false si no se insertó ninguna fila
    */

    public boolean insertEquivalence(int productA, int productB) {
        
        String sql = "INSERT INTO EQUIVALENCES (PRODUCT_ID_A, PRODUCT_ID_B) VALUES (?, ?)";

        try (Connection con = DB.getConnection();
            
            PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, productA);
            stmt.setInt(2, productB);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error en EquivalenceDAO.insertEquivalence: " + e.getMessage(), e);
        }
    }

    /**
     * Devuelve la lista de productos equivalentes a un producto.
     * La consulta busca en EQUIVALENCES si el producto está en A o en B,
     * y en base a eso devuelve el "otro" producto de la pareja.
     *
     * @param productId id del producto del que quiero obtener equivalencias
     * @return lista de productos equivalentes; si no hay, devuelve lista vacía
    */
   
    public List<Product> findEquivalents(int productId) {

        String sql =
            "SELECT P.ID_PRODUCT, P.CLIENT_ID, P.NAME \n" +
            "FROM EQUIVALENCES E \n" +
            "JOIN PRODUCTS P ON P.ID_PRODUCT = CASE \n" +
            "WHEN E.PRODUCT_ID_A = ? THEN E.PRODUCT_ID_B \n" +
            "ELSE E.PRODUCT_ID_A \n" +
            "END \n" +
            "WHERE E.PRODUCT_ID_A = ? OR E.PRODUCT_ID_B = ?";

        List<Product> equivalents = new ArrayList<>();

        try (Connection con = DB.getConnection();
            
            PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, productId);
            stmt.setInt(2, productId);
            stmt.setInt(3, productId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    equivalents.add(new Product(
                        rs.getInt("ID_PRODUCT"),
                        rs.getInt("CLIENT_ID"),
                        rs.getString("NAME")
                    ));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error en EquivalenceDAO.findEquivalents: " + e.getMessage(), e);
        }

        return equivalents;
    }
}