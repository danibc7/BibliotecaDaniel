package com.bibliotecadaniel.bibliotecadaniel.dto;

public class DesarrolladoraDTO {
    private int id;
    private String nombre;

    public DesarrolladoraDTO(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }
}
