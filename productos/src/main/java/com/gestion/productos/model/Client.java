package com.gestion.productos.model;

public class Client {

    private int id;
    private String code;
    private String name;

    public Client() {
    }

    public Client(int id, String code, String name) {
        this.id = id;
        this.code = code;
        this.name = name;
    }

    public int getId() {return id;}
    public String getCode() {return code;}
    public String getName() {return name;}

    public void setId(int id) {this.id = id;}
    public void setCode(String code) {this.code = code;}
    public void setName(String name) {this.name = name;}

    // Para imprimir bonito en consola
    @Override
    public String toString() {
        return "Client{id=" + id + ", code='" + code + "', name='" + name + "'}";
    }
}
