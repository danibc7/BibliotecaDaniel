package com.bibliotecadaniel.bibliotecadaniel.dto;

public class VideojuegoMasPrestadoDTO {

    private String titulo;
    private long numeroPrestamos;

    public VideojuegoMasPrestadoDTO(String titulo, long numeroPrestamos) {
        this.titulo = titulo;
        this.numeroPrestamos = numeroPrestamos;
    }

    public String getTitulo() {
        return titulo;
    }

    public long getNumeroPrestamos() {
        return numeroPrestamos;
    }
}
