
package com.bibliotecadaniel.bibliotecadaniel.service;

import com.bibliotecadaniel.bibliotecadaniel.dao.IPrestamoDAO;
import com.bibliotecadaniel.bibliotecadaniel.dto.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
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

    public List<PrestamoAbiertoDTO> listarPrestamosAbiertos(String socio) {

        return prestamoDAO.listarPrestamosAbiertos(socio);
    }

    public BigDecimal devolverPrestamo(int idPrestamo, boolean copiaDanada) {

        if (idPrestamo <= 0) {
            throw new IllegalArgumentException("El identificador del préstamo no es válido.");
        }
        return prestamoDAO.devolverPrestamo(idPrestamo, copiaDanada);
    }

    public ResumenDevolucionDTO obtenerResumenDevolucion(int idPrestamo) {

        if (idPrestamo <= 0) {
            throw new IllegalArgumentException("El identificador del préstamo no es válido.");
        }

        return prestamoDAO.obtenerResumenDevolucion(idPrestamo);
    }

    public LocalDate renovarPrestamo(int idPrestamo) {
        if (idPrestamo <= 0) {
            throw new IllegalArgumentException("El identificador del préstamo no es válido.");
        }
        return prestamoDAO.renovarPrestamo(idPrestamo);
    }

    public List<PrestamoRestDTO> listarPrestamosRest(String socio, String titulo, String estado, LocalDate fechaDesde) {

        if (estado != null && !estado.equals("EN_PLAZO")
                && !estado.equals("VENCIDO")
                && !estado.equals("DEVUELTO")) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El estado debe ser EN_PLAZO, VENCIDO o DEVUELTO");
        }

        return prestamoDAO.listarPrestamosRest(socio, titulo, estado, fechaDesde);
    }

    public PrestamoRestDTO buscarPrestamoRestPorId(int id) {

        PrestamoRestDTO prestamo = prestamoDAO.buscarPrestamoRestPorId(id);

        if (prestamo == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El préstamo no existe");
        }

        return prestamo;
    }
}
