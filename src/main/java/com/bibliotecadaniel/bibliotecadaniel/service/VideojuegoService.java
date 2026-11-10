package com.bibliotecadaniel.bibliotecadaniel.service;

import com.bibliotecadaniel.bibliotecadaniel.dao.IVideojuegoDAO;
import com.bibliotecadaniel.bibliotecadaniel.dto.VideojuegoDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VideojuegoService {
    private final IVideojuegoDAO videojuegoDAO;
    private final DesarrolladoraService desarrolladoraService;

    public VideojuegoService(IVideojuegoDAO videojuegoDAO, DesarrolladoraService desarrolladoraService) {
        this.videojuegoDAO = videojuegoDAO;
        this.desarrolladoraService = desarrolladoraService;
    }


    public List<VideojuegoDTO> buscarPorTitulo(String titulo, Integer idDesarrolladora, Integer anioDesde, Integer pegiMaximo, boolean soloDisponibles) {

        return videojuegoDAO.buscarPorTitulo(titulo, idDesarrolladora, anioDesde,pegiMaximo, soloDisponibles);
    }

    public void guardarVideojuego(String titulo, int anioLanzamiento, int pegi, int idDesarrolladora) {

        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("El título del videojuego es obligatorio");
        }

        int anioActual = java.time.Year.now().getValue();

        if (anioLanzamiento < 1970 || anioLanzamiento > anioActual) {
            throw new IllegalArgumentException("El año debe estar entre 1970 y " + anioActual);
        }

        if (pegi != 3 && pegi != 7 && pegi != 12 && pegi != 16 && pegi != 18) {
            throw new IllegalArgumentException("La clasificación PEGI no es válida");
        }

        if (idDesarrolladora <= 0) {
            throw new IllegalArgumentException("Debes seleccionar una desarrolladora válida");
        }

        if (!desarrolladoraService.existePorId(idDesarrolladora)) {
            throw new IllegalArgumentException("La desarrolladora seleccionada no existe");
        }

        videojuegoDAO.guardarVideojuego(titulo.trim(), anioLanzamiento, pegi, idDesarrolladora);
    }


}
