package com.bibliotecadaniel.bibliotecadaniel.dao;

import com.bibliotecadaniel.bibliotecadaniel.dto.EstadisticasDTO;
import com.bibliotecadaniel.bibliotecadaniel.repository.EstadisticasRepository;
import org.springframework.stereotype.Repository;

@Repository
public class EstadisticasDAO implements IEstadisticasDAO {

    private final EstadisticasRepository estadisticasRepository;

    public EstadisticasDAO(
            EstadisticasRepository estadisticasRepository) {

        this.estadisticasRepository = estadisticasRepository;
    }

    @Override
    public EstadisticasDTO obtenerEstadisticas() {

        return estadisticasRepository.obtenerEstadisticas();
    }
}
