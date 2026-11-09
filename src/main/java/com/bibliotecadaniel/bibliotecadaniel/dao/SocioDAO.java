
package com.bibliotecadaniel.bibliotecadaniel.dao;

import com.bibliotecadaniel.bibliotecadaniel.dto.SocioDTO;
import com.bibliotecadaniel.bibliotecadaniel.repository.SocioRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SocioDAO {

    private final SocioRepository socioRepository;

    public SocioDAO(SocioRepository socioRepository) {
        this.socioRepository = socioRepository;
    }

    public List<SocioDTO> listarTodos() {
        return socioRepository.listarTodos();
    }

    public List<SocioDTO> buscarPorNombreApellidos(String nombreApellidos, String email, String estado) {

        return socioRepository.buscarPorNombreApellidos(nombreApellidos, email, estado);
    }



}
