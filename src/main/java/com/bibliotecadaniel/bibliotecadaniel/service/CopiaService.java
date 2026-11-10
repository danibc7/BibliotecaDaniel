
package com.bibliotecadaniel.bibliotecadaniel.service;

import com.bibliotecadaniel.bibliotecadaniel.dao.ICopiaDAO;
import com.bibliotecadaniel.bibliotecadaniel.dto.CopiaDisponibleDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CopiaService {

    private final ICopiaDAO copiaDAO;

    public CopiaService(ICopiaDAO copiaDAO) {
        this.copiaDAO = copiaDAO;
    }

    public List<CopiaDisponibleDTO> listarDisponibles() {
        return copiaDAO.listarDisponibles();
    }
}
