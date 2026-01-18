package com.gestion.productos.model;

public class Product {
    
    private int id;
    private int clienteID;
    private String name;

    public Product() {

    }

    public Product(int id, int clienteID, String name) {
        this.id = id;
        this.clienteID = clienteID;
        this.name = name;
    }

    public int getId(){return id;}
    public int getClienteID(){return clienteID;}
    public String getName(){return name;}

    public void setId(int id){this.id = id;}
    public void setclienteID(int clienteID){this.clienteID = clienteID;}
    public void setName(String name){this.name = name;}

    // Para imprimir bonito en consola
    @Override
    public String toString() {
        return "Product{id=" + id + ", ClientID='" + clienteID + "', name='" + name + "'}";
    }
}
