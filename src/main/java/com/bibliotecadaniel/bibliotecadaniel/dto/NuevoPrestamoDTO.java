
package com.bibliotecadaniel.bibliotecadaniel.dto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Datos necesarios para registrar un nuevo préstamo")
public class NuevoPrestamoDTO {

    @Schema(description = "Identificador del socio", example = "13")
    private Integer idSocio;

    @Schema(description = "Identificador de la copia del videojuego", example = "17")
    private Integer idCopia;

    public NuevoPrestamoDTO() {
    }

    public Integer getIdSocio() {
        return idSocio;
    }
    public void setIdSocio(Integer idSocio) {
        this.idSocio = idSocio;
    }

    public Integer getIdCopia() {
        return idCopia;
    }

    public void setIdCopia(Integer idCopia) {
        this.idCopia = idCopia;
    }
}
