
package com.bibliotecadaniel.bibliotecadaniel.service;
import com.bibliotecadaniel.bibliotecadaniel.dao.IDesarrolladoraDAO;
import com.bibliotecadaniel.bibliotecadaniel.dto.DesarrolladoraDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DesarrolladoraService {

    private final IDesarrolladoraDAO desarrolladoraDAO;

    public DesarrolladoraService(IDesarrolladoraDAO desarrolladoraDAO) {
        this.desarrolladoraDAO = desarrolladoraDAO;
    }

    public List<DesarrolladoraDTO> listarTodas() {
        return desarrolladoraDAO.listarTodas();
    }

    public boolean existePorId(int idDesarrolladora) {

        return desarrolladoraDAO.existePorId(idDesarrolladora);
    }

}
