
package com.bibliotecadaniel.bibliotecadaniel.dto;

public class EstadisticasDTO {

    private int totalVideojuegos;
    private int sociosActivos;
    private int prestamosAbiertos;
    private int prestamosVencidos;

    public EstadisticasDTO(int totalVideojuegos, int sociosActivos, int prestamosAbiertos, int prestamosVencidos) {

        this.totalVideojuegos = totalVideojuegos;
        this.sociosActivos = sociosActivos;
        this.prestamosAbiertos = prestamosAbiertos;
        this.prestamosVencidos = prestamosVencidos;
    }

    public int getTotalVideojuegos() {
        return totalVideojuegos;
    }

    public int getSociosActivos() {
        return sociosActivos;
    }

    public int getPrestamosAbiertos() {
        return prestamosAbiertos;
    }

    public int getPrestamosVencidos() {
        return prestamosVencidos;
    }
}

