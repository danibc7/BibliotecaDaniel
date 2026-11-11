
package com.bibliotecadaniel.bibliotecadaniel.service;

import com.bibliotecadaniel.bibliotecadaniel.dao.IPrestamoDAO;
import com.bibliotecadaniel.bibliotecadaniel.dto.NuevoPrestamoDTO;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.Period;
import com.bibliotecadaniel.bibliotecadaniel.dto.ResumenPrestamoDTO;
import com.bibliotecadaniel.bibliotecadaniel.dto.PrestamoListadoDTO;
import java.util.List;

@Service
public class PrestamoService {

    private final IPrestamoDAO prestamoDAO;

    public PrestamoService(IPrestamoDAO prestamoDAO) {
        this.prestamoDAO = prestamoDAO;
    }

    public ResumenPrestamoDTO registrarPrestamo(NuevoPrestamoDTO prestamo) {

        if (prestamo == null || prestamo.getIdSocio() == null || prestamo.getIdCopia() == null) {

            throw new IllegalArgumentException("Debes seleccionar un socio y una copia");
        }

        int idSocio = prestamo.getIdSocio();
        int idCopia = prestamo.getIdCopia();

        if (prestamoDAO.contarPrestamosAbiertos(idSocio) >= 3) {
            throw new IllegalArgumentException("El socio ya tiene 3 préstamos abiertos");
        }

        if (prestamoDAO.tienePrestamosVencidos(idSocio)) {
            throw new IllegalArgumentException("El socio tiene préstamos vencidos pendientes de devolución");
        }

        if (!prestamoDAO.copiaDisponible(idCopia)) {
            throw new IllegalArgumentException("La copia seleccionada no está disponible");
        }

        int[] datosVideojuego = prestamoDAO.obtenerDatosVideojuego(idCopia);

        int pegi = datosVideojuego[0];

        LocalDate fechaNacimiento = prestamoDAO.obtenerNacimientoSocio(idSocio);

        int edad = Period.between(
                fechaNacimiento, LocalDate.now()).getYears();

        if (edad < pegi) {
            throw new IllegalArgumentException("El socio no tiene la edad mínima para este videojuego");
        }

        LocalDate fechaPrestamo = LocalDate.now();

        int anioActual = fechaPrestamo.getYear();

        int anioLanzamiento = datosVideojuego[1];

        int diasPrestamo;

        if (anioLanzamiento >= anioActual - 1) {
            diasPrestamo = 7;
        } else {
            diasPrestamo = 15;
        }

        LocalDate fechaLimite = fechaPrestamo.plusDays(diasPrestamo);

        prestamoDAO.registrarPrestamoSeguro(idSocio, idCopia, fechaPrestamo, fechaLimite);

        return prestamoDAO.obtenerResumenPrestamo(idSocio,idCopia,fechaPrestamo,fechaLimite);
    }

    public List<PrestamoListadoDTO> listarPrestamos(String socio, String videojuego, String estado, LocalDate fechaDesde) {

        return prestamoDAO.listarPrestamos(socio, videojuego, estado, fechaDesde);
    }

}
