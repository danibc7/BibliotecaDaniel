package com.bibliotecadaniel.bibliotecadaniel.dto;

public class VideojuegoDTO {

    private int id;
    private String titulo;
    private int anioLanzamiento;
    private int pegi;
    private int idDesarrolladora;

    public VideojuegoDTO(int id, String titulo, int anioLanzamiento, int pegi, int idDesarrolladora) {
        this.id = id;
        this.titulo = titulo;
        this.anioLanzamiento = anioLanzamiento;
        this.pegi = pegi;
        this.idDesarrolladora = idDesarrolladora;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getAnioLanzamiento() {
        return anioLanzamiento;
    }

    public void setAnioLanzamiento(int anioLanzamiento) {
        this.anioLanzamiento = anioLanzamiento;
    }

    public int getPegi() {
        return pegi;
    }

    public void setPegi(int pegi) {
        this.pegi = pegi;
    }

    public int getIdDesarrolladora() {
        return idDesarrolladora;
    }

    public void setIdDesarrolladora(int idDesarrolladora) {
        this.idDesarrolladora = idDesarrolladora;
    }
}
