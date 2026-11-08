
package com.bibliotecadaniel.bibliotecadaniel.service;

import com.bibliotecadaniel.bibliotecadaniel.dao.DesarrolladoraDAO;
import com.bibliotecadaniel.bibliotecadaniel.dto.DesarrolladoraDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DesarrolladoraService {

    private final DesarrolladoraDAO desarrolladoraDAO;

    public DesarrolladoraService(DesarrolladoraDAO desarrolladoraDAO) {
        this.desarrolladoraDAO = desarrolladoraDAO;
    }

    public List<DesarrolladoraDTO> listarTodas() {
        return desarrolladoraDAO.listarTodas();
    }
}
