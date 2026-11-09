
package com.bibliotecadaniel.bibliotecadaniel.service;

import com.bibliotecadaniel.bibliotecadaniel.dao.SocioDAO;
import com.bibliotecadaniel.bibliotecadaniel.dto.SocioDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SocioService {

    private final SocioDAO socioDAO;

    public SocioService(SocioDAO socioDAO) {
        this.socioDAO = socioDAO;
    }

    public List<SocioDTO> listarTodos() {
        return socioDAO.listarTodos();
    }

    public List<SocioDTO> buscarPorNombreApellidos(String nombreApellidos, String email, String estado) {

        return socioDAO.buscarPorNombreApellidos(nombreApellidos, email, estado);
    }




}
