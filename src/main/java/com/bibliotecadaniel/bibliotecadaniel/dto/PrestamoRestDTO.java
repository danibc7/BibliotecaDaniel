package com.bibliotecadaniel.bibliotecadaniel.dto;

import java.time.LocalDate;
import java.math.BigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Información completa de un préstamo")
public class PrestamoRestDTO {

    @Schema(description = "Identificador del préstamo", example = "27")
    private int id;

    @Schema(description = "Identificador del socio", example = "13")
    private int idSocio;

    @Schema(description = "Identificador de la copia", example = "7")
    private int idCopia;

    @Schema(description = "Nombre completo del socio", example = "Carlos García López")
    private String socio;

    @Schema(description = "Título del videojuego", example = "Mario Kart World")
    private String titulo;

    @Schema(description = "Plataforma del videojuego", example = "Nintendo Switch 2")
    private String plataforma;

    @Schema(description = "Código de inventario de la copia", example = "CP-0007")
    private String codigoInventario;

    @Schema(description = "Fecha de registro del préstamo", example = "2026-11-23")
    private LocalDate fechaPrestamo;

    @Schema(description = "Fecha límite para devolver el videojuego", example = "2026-11-30")
    private LocalDate fechaLimite;

    @Schema(description = "Fecha de devolución, si ya se ha devuelto", example = "2026-11-24", nullable = true)
    private LocalDate fechaDevolucion;

    @Schema(description = "Multa en euros por retraso", example = "1.50")
    private BigDecimal multa;

    @Schema(description = "Número de renovaciones del préstamo", example = "0")
    private int renovaciones;

    @Schema(description = "Estado del préstamo", example = "DEVUELTO", allowableValues = {"EN_PLAZO", "VENCIDO", "DEVUELTO"})
    private String estado;

    @Schema(description = "Días de retraso del préstamo", example = "3")
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