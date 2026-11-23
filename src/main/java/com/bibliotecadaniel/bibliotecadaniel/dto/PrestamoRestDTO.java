package com.bibliotecadaniel.bibliotecadaniel.dto;

import java.time.LocalDate;
import java.math.BigDecimal;

public class PrestamoRestDTO {

    private int id;
    private int idSocio;
    private int idCopia;
    private String socio;
    private String titulo;
    private String plataforma;
    private String codigoInventario;
    private LocalDate fechaPrestamo;
    private LocalDate fechaLimite;
    private LocalDate fechaDevolucion;
    private BigDecimal multa;
    private int renovaciones;
    private String estado;
    private long diasRetraso;

    public PrestamoRestDTO(int id, int idSocio, int idCopia, String socio, String titulo, String plataforma, String codigoInventario, LocalDate fechaPrestamo,
                           LocalDate fechaLimite, LocalDate fechaDevolucion, BigDecimal multa, int renovaciones, String estado, long diasRetraso) {

        this.id = id;
        this.idSocio = idSocio;
        this.idCopia = idCopia;
        this.socio = socio;
        this.titulo = titulo;
        this.plataforma = plataforma;
        this.codigoInventario = codigoInventario;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaLimite = fechaLimite;
        this.fechaDevolucion = fechaDevolucion;
        this.multa = multa;
        this.renovaciones = renovaciones;
        this.estado = estado;
        this.diasRetraso = diasRetraso;
    }

    public int getId() {
        return id;
    }

    public int getIdSocio() {
        return idSocio;
    }

    public int getIdCopia() {
        return idCopia;
    }

    public String getSocio() {
        return socio;
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

    public int getRenovaciones() {
        return renovaciones;
    }

    public String getEstado() {
        return estado;
    }

    public long getDiasRetraso() {
        return diasRetraso;
    }
}