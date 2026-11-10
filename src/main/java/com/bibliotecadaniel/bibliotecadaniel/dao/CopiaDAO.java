
package com.bibliotecadaniel.bibliotecadaniel.dao;

import com.bibliotecadaniel.bibliotecadaniel.dto.CopiaDisponibleDTO;
import com.bibliotecadaniel.bibliotecadaniel.repository.CopiaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CopiaDAO implements ICopiaDAO {

    private final CopiaRepository copiaRepository;

    public CopiaDAO(CopiaRepository copiaRepository) {
        this.copiaRepository = copiaRepository;
    }

    @Override
    public List<CopiaDisponibleDTO> listarDisponibles() {

        return copiaRepository.listarDisponibles();
    }
}
