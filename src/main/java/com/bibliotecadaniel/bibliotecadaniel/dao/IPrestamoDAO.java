package com.bibliotecadaniel.bibliotecadaniel.dao;

import java.time.LocalDate;
import java.util.List;

import com.bibliotecadaniel.bibliotecadaniel.dto.PrestamoListadoDTO;
import com.bibliotecadaniel.bibliotecadaniel.dto.ResumenPrestamoDTO;

public interface IPrestamoDAO {

    int contarPrestamosAbiertos(int idSocio);

    boolean tienePrestamosVencidos(int idSocio);

    boolean copiaDisponible(int idCopia);

    int[] obtenerDatosVideojuego(int idCopia);

    LocalDate obtenerNacimientoSocio(int idSocio);

    void guardarPrestamo(int idSocio, int idCopia, LocalDate fechaPrestamo, LocalDate fechaLimite);

    void registrarPrestamoSeguro(int idSocio, int idCopia, LocalDate fechaPrestamo, LocalDate fechaLimite);

    ResumenPrestamoDTO obtenerResumenPrestamo(int idSocio, int idCopia, LocalDate fechaPrestamo, LocalDate fechaLimite);

    List<PrestamoListadoDTO> listarPrestamos(String socio, String videojuego, String estado, LocalDate fechaDesde);

}

