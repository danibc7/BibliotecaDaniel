
package com.bibliotecadaniel.bibliotecadaniel.dto;

import java.time.LocalDate;
import java.math.BigDecimal;

public class PrestamoListadoDTO {

    private int id;
    private String socio;
    private String videojuego;
    private String plataforma;
    private LocalDate fechaPrestamo;
    private LocalDate fechaLimite;
    private LocalDate fechaDevolucion;
    private BigDecimal multa;
    private String estado;
    private long diasRetraso;

    public PrestamoListadoDTO(int id, String socio, String videojuego, String plataforma, LocalDate fechaPrestamo,
                              LocalDate fechaLimite, LocalDate fechaDevolucion, BigDecimal multa, String estado, long diasRetraso) {

        this.id = id;
        this.socio = socio;
        this.videojuego = videojuego;
        this.plataforma = plataforma;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaLimite = fechaLimite;
        this.fechaDevolucion = fechaDevolucion;
        this.multa = multa;
        this.estado = estado;
        this.diasRetraso = diasRetraso;
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

    public LocalDate getFechaPrestamo() {
        return fechaPrestamo;
    }

    public LocalDate getFechaLimite() {
        return fechaLimite;
    }

    public LocalDate getFechaDevolucion() {
        return fechaDevolucion;
    }

    public BigDecimal getMulta() {
        return multa;
    }

    public String getEstado() {
        return estado;
    }

    public long getDiasRetraso() {
        return diasRetraso;
    }
}

