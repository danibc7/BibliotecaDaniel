
package com.bibliotecadaniel.bibliotecadaniel.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ResumenDevolucionDTO {

    private String videojuego;
    private String plataforma;
    private String codigoInventario;
    private String socio;
    private LocalDate fechaPrestamo;
    private LocalDate fechaLimite;
    private LocalDate fechaDevolucion;
    private long diasRetraso;
    private BigDecimal multa;

    public ResumenDevolucionDTO(String videojuego, String plataforma, String codigoInventario, String socio,
                                LocalDate fechaPrestamo, LocalDate fechaLimite, LocalDate fechaDevolucion, long diasRetraso, BigDecimal multa) {

        this.videojuego = videojuego;
        this.plataforma = plataforma;
        this.codigoInventario = codigoInventario;
        this.socio = socio;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaLimite = fechaLimite;
        this.fechaDevolucion = fechaDevolucion;
        this.diasRetraso = diasRetraso;
        this.multa = multa;
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

    public String getSocio() {
        return socio;
    }

    public LocalDate getFechaPrestamo() {
        return fechaPrestamo;
    }

    public LocalDate getFechaLimite() {
        return fechaLimite;
    }

    public LocalDate getFechaDevolucion() {
        return fechaDevolucion;
    }

    public long getDiasRetraso() {
        return diasRetraso;
    }

    public BigDecimal getMulta() {
        return multa;
    }
}
