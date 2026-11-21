package com.bibliotecadaniel.bibliotecadaniel.service;

import com.bibliotecadaniel.bibliotecadaniel.dao.IEstadisticasDAO;
import com.bibliotecadaniel.bibliotecadaniel.dto.EstadisticasDTO;
import com.bibliotecadaniel.bibliotecadaniel.dto.VideojuegoMasPrestadoDTO;
import java.util.List;
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

    public List<VideojuegoMasPrestadoDTO> listarVideojuegosMasPrestados() {
        return estadisticasDAO.listarVideojuegosMasPrestados();
    }
}
