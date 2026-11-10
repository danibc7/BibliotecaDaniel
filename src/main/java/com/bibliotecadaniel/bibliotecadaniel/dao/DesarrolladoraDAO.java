
package com.bibliotecadaniel.bibliotecadaniel.dao;

import com.bibliotecadaniel.bibliotecadaniel.dto.DesarrolladoraDTO;
import com.bibliotecadaniel.bibliotecadaniel.repository.DesarrolladoraRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DesarrolladoraDAO implements IDesarrolladoraDAO {

    private final DesarrolladoraRepository desarrolladoraRepository;

    public DesarrolladoraDAO(DesarrolladoraRepository desarrolladoraRepository) {
        this.desarrolladoraRepository = desarrolladoraRepository;
    }

    @Override
    public List<DesarrolladoraDTO> listarTodas() {
        return desarrolladoraRepository.listarTodas();
    }

    @Override
    public boolean existePorId(int idDesarrolladora) {
        return desarrolladoraRepository.existePorId(idDesarrolladora);
    }
}
