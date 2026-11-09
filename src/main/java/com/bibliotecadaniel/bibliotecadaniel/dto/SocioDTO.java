
package com.bibliotecadaniel.bibliotecadaniel.dto;

import java.time.LocalDate;

public class SocioDTO {

    private int id;
    private String nombre;
    private String apellidos;
    private String email;
    private LocalDate fechaNacimiento;
    private boolean activo;
    private int edad;
    private int prestamosAbiertos;


    public SocioDTO(int id, String nombre, String apellidos, String email, LocalDate fechaNacimiento, boolean activo, int edad, int prestamosAbiertos) {

        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.fechaNacimiento = fechaNacimiento;
        this.activo = activo;
        this.edad = edad;
        this.prestamosAbiertos = prestamosAbiertos;
    }


    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public boolean isActivo() {
        return activo;
    }

    public int getEdad() {
        return edad;
    }

    public int getPrestamosAbiertos() {
        return prestamosAbiertos;
    }
}

