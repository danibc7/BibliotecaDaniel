
package com.bibliotecadaniel.bibliotecadaniel.dto;

import java.time.LocalDate;

public class ResumenPrestamoDTO {

    private String socio;
    private String videojuego;
    private String plataforma;
    private String codigoInventario;
    private LocalDate fechaPrestamo;
    private LocalDate fechaLimite;

    public ResumenPrestamoDTO(String socio, String videojuego, String plataforma, String codigoInventario, LocalDate fechaPrestamo, LocalDate fechaLimite) {

        this.socio = socio;
        this.videojuego = videojuego;
        this.plataforma = plataforma;
        this.codigoInventario = codigoInventario;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaLimite = fechaLimite;
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

    public LocalDate getFechaPrestamo() {
        return fechaPrestamo;
    }

    public LocalDate getFechaLimite() {
        return fechaLimite;
    }
}
