
package com.bibliotecadaniel.bibliotecadaniel.dao;

import com.bibliotecadaniel.bibliotecadaniel.dto.NuevoSocioDTO;
import com.bibliotecadaniel.bibliotecadaniel.dto.SocioDTO;
import com.bibliotecadaniel.bibliotecadaniel.repository.SocioRepository;
import org.springframework.stereotype.Repository;
import com.bibliotecadaniel.bibliotecadaniel.dto.ActualizarSocioDTO;

import java.util.List;

@Repository
public class SocioDAO implements ISocioDAO {

    private final SocioRepository socioRepository;

    public SocioDAO(SocioRepository socioRepository) {
        this.socioRepository = socioRepository;
    }

    @Override
    public List<SocioDTO> listarTodos() {
        return socioRepository.listarTodos();
    }

    @Override
    public List<SocioDTO> buscarPorNombreApellidos(String nombreApellidos, String email, String estado) {

        return socioRepository.buscarPorNombreApellidos(nombreApellidos, email, estado);
    }
    @Override
    public int guardarSocio(NuevoSocioDTO socio) {
        return socioRepository.guardarSocio(socio);
    }
    @Override
    public boolean existeEmail(String email) {

        return socioRepository.existeEmail(email);
    }

    @Override
    public List<SocioDTO> listarActivos() {
        return socioRepository.listarActivos();
    }

    @Override
    public SocioDTO buscarPorId(int id) {
        return socioRepository.buscarPorId(id);
    }

    @Override
    public int actualizarSocio(int id, ActualizarSocioDTO socio) {
        return socioRepository.actualizarSocio(id, socio);
    }

    @Override
    public boolean tienePrestamos(int id) {
        return socioRepository.tienePrestamos(id);
    }

    @Override
    public int eliminarSocio(int id) {
        return socioRepository.eliminarSocio(id);
    }
}
