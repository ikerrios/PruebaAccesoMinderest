package com.gestion.productos.services;

import com.gestion.productos.dao.ClientDAO;
import com.gestion.productos.dao.EquivalenceDAO;
import com.gestion.productos.dao.ProductDAO;
import com.gestion.productos.model.Client;
import com.gestion.productos.model.Product;

import java.util.List;

/**
 * Capa Service: aquí centralizo la lógica de negocio.
 * Los DAO se encargan de acceder a la base de datos y este Service decide reglas,
 * validaciones y el flujo de cada caso de uso.
 */
public class Service {

    private final ClientDAO clientDAO;
    private final ProductDAO productDAO;
    private final EquivalenceDAO equivalenceDAO;

    public Service() {
        this.clientDAO = new ClientDAO();
        this.productDAO = new ProductDAO();
        this.equivalenceDAO = new EquivalenceDAO();
    }

    /**
     * Da de alta un producto para un cliente identificado por su código.
     *
     * @param clientCode  código del cliente (por ejemplo C001)
     * @param productName nombre del producto
     * @return mensaje con el resultado de la operación (éxito o motivo del fallo)
     */
    public String altaProducto(String clientCode, String productName) {

        if (isEmpty(clientCode) || isEmpty(productName)) {
            return "Error: faltan datos.";
        }

        Client client = clientDAO.findByCodigo(clientCode.trim());
        if (client == null) {
            return "No existe el cliente con código: " + clientCode;
        }

        // Aquí casteo a int porque en mi tabla el ID es int y mi DAO trabaja con int
        int clientId = (int) client.getId();

        int newId = productDAO.insertProduct(clientId, productName.trim());
        if (newId == -1) {
            return "No se pudo insertar el producto.";
        }

        return "Producto añadido correctamente (ID_PRODUCT=" + newId + ").";
    }

    /**
     * Establece una equivalencia entre dos productos (de clientes distintos).
     * Se busca el cliente por code, luego el producto por (clientId + nombre),
     * y finalmente se crea la equivalencia si no existe ya.
     *
     * @param clientCodeA  código cliente A
     * @param productNameA producto A
     * @param clientCodeB  código cliente B
     * @param productNameB producto B
     * @return mensaje con el resultado de la operación
     */
    public String establecerEquivalencia(String clientCodeA, String productNameA, String clientCodeB, String productNameB) {

        if (isEmpty(clientCodeA) || isEmpty(productNameA) || isEmpty(clientCodeB) || isEmpty(productNameB)) {
            return "Error: faltan datos.";
        }

        Client clientA = clientDAO.findByCodigo(clientCodeA.trim());
        Client clientB = clientDAO.findByCodigo(clientCodeB.trim());

        if (clientA == null) return "No existe el cliente A con código: " + clientCodeA;
        if (clientB == null) return "No existe el cliente B con código: " + clientCodeB;

        // Esta regla la pongo aquí porque es lógica de negocio: no tiene sentido equivaler productos del mismo cliente
        if (clientA.getId() == clientB.getId()) {
            return "No se puede establecer equivalencia entre productos del mismo cliente.";
        }

        int clientIdA = (int) clientA.getId();
        int clientIdB = (int) clientB.getId();

        // Yo busco el producto por cliente + nombre para que el nombre pueda repetirse en distintos clientes
        Product productA = productDAO.finByClientAndName(clientIdA, productNameA.trim());
        Product productB = productDAO.finByClientAndName(clientIdB, productNameB.trim());

        if (productA == null) return "No existe el producto A para ese cliente.";
        if (productB == null) return "No existe el producto B para ese cliente.";

        // Normalizo el orden (min/max) para guardar siempre la pareja igual y evitar duplicados invertidos (A,B) vs (B,A)
        int a = Math.min(productA.getId(), productB.getId());
        int b = Math.max(productA.getId(), productB.getId());

        if (equivalenceDAO.existsEquivalence(a, b)) {
            return "La equivalencia ya existe.";
        }

        boolean inserted = equivalenceDAO.insertEquivalence(a, b);
        if (!inserted) {
            return "No se pudo crear la equivalencia.";
        }

        return "Equivalencia creada correctamente.";
    }

    /**
     * Lista los productos equivalentes a un producto concreto (identificado por cliente + nombre).
     *
     * @param clientCode  código del cliente
     * @param productName nombre del producto
     * @return lista de productos equivalentes; si algo falla devuelve lista vacía
     */
    public List<Product> listarEquivalentes(String clientCode, String productName) {

        if (isEmpty(clientCode) || isEmpty(productName)) {
            return List.of();
        }

        Client client = clientDAO.findByCodigo(clientCode.trim());
        if (client == null) {
            return List.of();
        }

        int clientId = (int) client.getId();

        Product product = productDAO.finByClientAndName(clientId, productName.trim());
        if (product == null) {
            return List.of();
        }

        // Devuelvo la lista y que el Main decida cómo mostrarla (separo lógica de presentación)
        return equivalenceDAO.findEquivalents(product.getId());
    }

    public List<Product> posiblesMismosProductos(String clientCode, String productName) {

        if (isEmpty(clientCode) || isEmpty(productName)) {
            return List.of();
        }

        Client client = clientDAO.findByCodigo(clientCode.trim());
        
        if (client == null) {
            return List.of();
        }

        int clientId = (int) client.getId();

        Product product = productDAO.finByClientAndName(clientId, productName.trim());
        
        if (product == null) {
            return List.of();
        }

        return productDAO.findSameNameInOtherClients(clientId, product.getName());
    }


    public List<Client> listarClientes() {
        return clientDAO.findAll();
    }

    public List<Product> listarProductos() {
        return productDAO.findAll();
    }


    /**
     * Validación simple para entradas de consola.
     * @param s texto a validar
     * @return true si es null o está vacío (ignorando espacios)
    */
    private boolean isEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }
}
