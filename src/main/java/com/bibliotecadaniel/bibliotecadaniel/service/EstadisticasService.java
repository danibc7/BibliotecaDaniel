package com.bibliotecadaniel.bibliotecadaniel.service;

import com.bibliotecadaniel.bibliotecadaniel.dao.IEstadisticasDAO;
import com.bibliotecadaniel.bibliotecadaniel.dto.EstadisticasDTO;
import org.springframework.stereotype.Service;

@Service
public class EstadisticasService {

    private final IEstadisticasDAO estadisticasDAO;

    public EstadisticasService(IEstadisticasDAO estadisticasDAO) {
        this.estadisticasDAO = estadisticasDAO;
    }

    public EstadisticasDTO obtenerEstadisticas() {

        return estadisticasDAO.obtenerEstadisticas();
    }
}
