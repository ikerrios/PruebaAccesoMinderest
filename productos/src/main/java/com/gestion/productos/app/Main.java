package com.gestion.productos.app;

import com.gestion.productos.model.Product;
import com.gestion.productos.services.Service;
import com.gestion.productos.model.Client;

import java.util.List;
import java.util.Scanner;

/**
 * Punto de entrada de la aplicación.
 * Menú por consola para ejecutar los casos de uso de la prueba:
 * 1) Alta de producto
 * 2) Establecer equivalencia
 * 3) Posibles "mismos productos" en otros clientes (candidatos)
 * 4) Ver equivalencias ya establecidas de un producto
 */
public class Main {

    public static void main(String[] args) {

        Service service = new Service();
        Scanner sc = new Scanner(System.in);
        int opcion;

        do {
            mostrarMenu();
            opcion = leerInt(sc);

            switch (opcion) {
                case 1 -> casoAltaProducto(sc, service);
                case 2 -> casoEstablecerEquivalencia(sc, service);
                case 3 -> casoVerPosiblesMismos(sc, service);
                case 4 -> casoVerEquivalencias(sc, service);
                case 0 -> System.out.println("Saliendo...");
                default -> System.out.println("Opcion no valida.");
            }

        } while (opcion != 0);

        sc.close();
    }

    /**
     * Gestiona la opción 1 del menú.
     * Pide los datos necesarios y llama al servicio para dar de alta el producto.
     */
    private static void casoAltaProducto(Scanner sc, Service service) {
        System.out.print("Codigo cliente (ej: C001): ");
        String clientCode = sc.nextLine();

        System.out.print("Nombre del producto: ");
        String productName = sc.nextLine();

        String res = service.altaProducto(clientCode, productName);
        System.out.println(res);
    }

    /**
     * Gestiona la opción 2 del menú.
     * Muestra listados de clientes y productos para ayudar, y luego pide los datos para crear la equivalencia.
     */
    private static void casoEstablecerEquivalencia(Scanner sc, Service service) {
        System.out.println("\n=== CLIENTES ===");
        System.out.println("ID_CLIENT | CODE  | NAME");
        System.out.println("----------------------------------------");
        for (Client c : service.listarClientes()) {
            System.out.println(c.getId() + " | " + c.getCode() + " | " + c.getName());
        }

        System.out.println("\n=== PRODUCTOS ===");
        System.out.println("ID_PRODUCT | CLIENT_ID | NAME");
        System.out.println("----------------------------------------");
        for (Product p : service.listarProductos()) {
            System.out.println(p.getId() + " | " + p.getClienteID() + " | " + p.getName());
        }
        System.out.println();

        System.out.print("Codigo cliente A (ej: C001): ");
        String clientCodeA = sc.nextLine();

        System.out.print("Nombre producto A: ");
        String productNameA = sc.nextLine();

        System.out.print("Codigo cliente B (ej: C002): ");
        String clientCodeB = sc.nextLine();

        System.out.print("Nombre producto B: ");
        String productNameB = sc.nextLine();

        String res = service.establecerEquivalencia(clientCodeA, productNameA, clientCodeB, productNameB);
        System.out.println(res);
    }

    /**
     * Gestiona la opción 3 del menú.
     * Busca posibles candidatos a ser el mismo producto en otros clientes.
     */
    private static void casoVerPosiblesMismos(Scanner sc, Service service) {
        System.out.print("Codigo cliente (ej: C001): ");
        String clientCode = sc.nextLine();

        System.out.print("Nombre del producto: ");
        String productName = sc.nextLine();

        List<Product> candidatos = service.posiblesMismosProductos(clientCode, productName);

        imprimirListaProductos(candidatos, "Posibles mismos productos (otros clientes)");
    }

    /**
     * Gestiona la opción 4 del menú.
     * Muestra las equivalencias ya establecidas en la base de datos.
     */
    private static void casoVerEquivalencias(Scanner sc, Service service) {
        System.out.print("Codigo cliente (ej: C001): ");
        String clientCode = sc.nextLine();

        System.out.print("Nombre del producto: ");
        String productName = sc.nextLine();

        List<Product> eq = service.listarEquivalentes(clientCode, productName);

        imprimirListaProductos(eq, "Productos equivalentes");
    }

    private static void mostrarMenu() {
        System.out.println("\n==== MENU MINDEREST ====");
        System.out.println("1. Alta producto");
        System.out.println("2. Establecer equivalencia");
        System.out.println("3. Ver posibles mismos productos (otros clientes)");
        System.out.println("4. Ver equivalencias de un producto");
        System.out.println("0. Salir");
        System.out.print("Elige una opcion: ");
    }

    /**
     * Imprime una lista de productos con formato tabla.
     * Evita repetir el bucle de impresión en las opciones 3 y 4.
     */
    private static void imprimirListaProductos(List<Product> lista, String titulo) {
        if (lista.isEmpty()) {
            System.out.println("No se han encontrado resultados (o datos incorrectos).");
        } else {
            System.out.println("\n=== " + titulo + " ===");
            System.out.println("ID_PRODUCT | CLIENT_ID | NAME");
            System.out.println("----------------------------------------");
            for (Product p : lista) {
                System.out.println(p.getId() + " | " + p.getClienteID() + " | " + p.getName());
            }
        }
    }

    /**
     * Lee un entero desde consola de forma segura.
     * Si el usuario introduce algo que no es número, vuelvo a pedirlo.
     */
    private static int leerInt(Scanner sc) {
        while (true) {
            String input = sc.nextLine();
            try {
                return Integer.parseInt(input.trim());
            } catch (NumberFormatException e) {
                System.out.print("Mete un numero valido: ");
            }
        }
    }
}