package com.bibliotecadaniel.bibliotecadaniel.dto;

public class DesarrolladoraDTO {

    private int id;
    private String nombre;
    private String pais;

    public DesarrolladoraDTO(int id, String nombre, String pais) {
        this.id = id;
        this.nombre = nombre;
        this.pais = pais;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getPais() {
        return pais;
    }
}