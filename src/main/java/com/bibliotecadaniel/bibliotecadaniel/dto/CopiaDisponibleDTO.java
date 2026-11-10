
package com.bibliotecadaniel.bibliotecadaniel.dto;

public class CopiaDisponibleDTO {

    private int id;
    private String titulo;
    private String plataforma;
    private String codigoInventario;

    public CopiaDisponibleDTO(int id, String titulo, String plataforma, String codigoInventario) {
        this.id = id;
        this.titulo = titulo;
        this.plataforma = plataforma;
        this.codigoInventario = codigoInventario;
    }

    public int getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getPlataforma() {
        return plataforma;
    }

    public String getCodigoInventario() {
        return codigoInventario;
    }
}
