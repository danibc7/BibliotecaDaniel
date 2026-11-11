
package com.bibliotecadaniel.bibliotecadaniel.dao;
import com.bibliotecadaniel.bibliotecadaniel.repository.PrestamoRepository;
import org.springframework.stereotype.Repository;
import com.bibliotecadaniel.bibliotecadaniel.dto.ResumenPrestamoDTO;
import com.bibliotecadaniel.bibliotecadaniel.dto.PrestamoListadoDTO;
import java.util.List;
import java.time.LocalDate;

@Repository
public class PrestamoDAO implements IPrestamoDAO {

    private final PrestamoRepository prestamoRepository;

    public PrestamoDAO(PrestamoRepository prestamoRepository) {
        this.prestamoRepository = prestamoRepository;
    }

    @Override
    public int contarPrestamosAbiertos(int idSocio) {
        return prestamoRepository.contarPrestamosAbiertos(idSocio);
    }

    @Override
    public boolean tienePrestamosVencidos(int idSocio) {
        return prestamoRepository.tienePrestamosVencidos(idSocio);
    }

    @Override
    public boolean copiaDisponible(int idCopia) {
        return prestamoRepository.copiaDisponible(idCopia);
    }

    @Override
    public int[] obtenerDatosVideojuego(int idCopia) {
        return prestamoRepository.obtenerDatosVideojuego(idCopia);
    }

    @Override
    public LocalDate obtenerNacimientoSocio(int idSocio) {
        return prestamoRepository.obtenerNacimientoSocio(idSocio);
    }

    @Override
    public void guardarPrestamo(int idSocio, int idCopia, LocalDate fechaPrestamo, LocalDate fechaLimite) {

        prestamoRepository.guardarPrestamo(idSocio, idCopia, fechaPrestamo, fechaLimite);
    }

    @Override
    public void registrarPrestamoSeguro(int idSocio, int idCopia, LocalDate fechaPrestamo, LocalDate fechaLimite) {

        prestamoRepository.registrarPrestamoSeguro(idSocio, idCopia, fechaPrestamo, fechaLimite);
    }

    @Override
    public ResumenPrestamoDTO obtenerResumenPrestamo(int idSocio, int idCopia, LocalDate fechaPrestamo, LocalDate fechaLimite) {
        return prestamoRepository.obtenerResumenPrestamo(idSocio, idCopia, fechaPrestamo, fechaLimite);
    }

    @Override
    public List<PrestamoListadoDTO> listarPrestamos(String socio, String videojuego, String estado, LocalDate fechaDesde) {

        return prestamoRepository.listarPrestamos(socio, videojuego, estado, fechaDesde);
    }

}

