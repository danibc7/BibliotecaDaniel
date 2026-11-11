
package com.bibliotecadaniel.bibliotecadaniel.dto;

import java.time.LocalDate;

public class PrestamoAbiertoDTO {

    private int id;
    private String socio;
    private String videojuego;
    private String plataforma;
    private String codigoInventario;
    private LocalDate fechaLimite;
    private String estado;

    public PrestamoAbiertoDTO(int id, String socio, String videojuego, String plataforma, String codigoInventario, LocalDate fechaLimite, String estado) {

        this.id = id;
        this.socio = socio;
        this.videojuego = videojuego;
        this.plataforma = plataforma;
        this.codigoInventario = codigoInventario;
        this.fechaLimite = fechaLimite;
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public String getSocio() {
        return socio;
    }

    public String getVideojuego() {
        return videojuego;
    }

    public String getPlataforma() {
        return plataforma;
    }

    public String getCodigoInventario() {
        return codigoInventario;
    }

    public LocalDate getFechaLimite() {
        return fechaLimite;
    }

    public String getEstado() {
        return estado;
    }
}
