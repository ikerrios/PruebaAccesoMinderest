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
            System.out.println("\n==== MENU MINDEREST ====");
            System.out.println("1. Alta producto");
            System.out.println("2. Establecer equivalencia");
            System.out.println("3. Ver posibles mismos productos (otros clientes)");
            System.out.println("4. Ver equivalencias de un producto");
            System.out.println("0. Salir");
            System.out.print("Elige una opcion: ");

            opcion = leerInt(sc);

            switch (opcion) {
                case 1 -> {
                    System.out.print("Codigo cliente (ej: C001): ");
                    String clientCode = sc.nextLine();

                    System.out.print("Nombre del producto: ");
                    String productName = sc.nextLine();

                    String res = service.altaProducto(clientCode, productName);
                    System.out.println(res);
                }

                case 2 -> {

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

                case 3 -> {
                    System.out.print("Codigo cliente (ej: C001): ");
                    String clientCode = sc.nextLine();

                    System.out.print("Nombre del producto: ");
                    String productName = sc.nextLine();

                    // Esto es lo que pide el enunciado: candidatos en otros clientes
                    List<Product> candidatos = service.posiblesMismosProductos(clientCode, productName);

                    if (candidatos.isEmpty()) {
                        System.out.println("No se han encontrado posibles coincidencias en otros clientes (o datos incorrectos).");
                    } else {
                        System.out.println("\n=== Posibles mismos productos (otros clientes) ===");
                        System.out.println("ID_PRODUCT | CLIENT_ID | NAME");
                        System.out.println("----------------------------------------");
                        for (Product p : candidatos) {
                            System.out.println(p.getId() + " | " + p.getClienteID() + " | " + p.getName());
                        }
                    }
                }

                case 4 -> {
                    System.out.print("Codigo cliente (ej: C001): ");
                    String clientCode = sc.nextLine();

                    System.out.print("Nombre del producto: ");
                    String productName = sc.nextLine();

                    // Esto muestra equivalencias ya establecidas (tabla EQUIVALENCES)
                    List<Product> eq = service.listarEquivalentes(clientCode, productName);

                    if (eq.isEmpty()) {
                        System.out.println("No hay equivalencias (o datos incorrectos).");
                    } else {
                        System.out.println("\n=== Productos equivalentes ===");
                        System.out.println("ID_PRODUCT | CLIENT_ID | NAME");
                        System.out.println("----------------------------------------");
                        for (Product p : eq) {
                            System.out.println(p.getId() + " | " + p.getClienteID() + " | " + p.getName());
                        }
                    }
                }

                case 0 -> System.out.println("Saliendo...");

                default -> System.out.println("Opcion no valida.");
            }

        } while (opcion != 0);

        sc.close();
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
