package com.bibliotecadaniel.bibliotecadaniel.service;

import com.bibliotecadaniel.bibliotecadaniel.dao.VideojuegoDAO;
import com.bibliotecadaniel.bibliotecadaniel.dto.VideojuegoDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VideojuegoService {
    private final VideojuegoDAO videojuegoDAO;

    public VideojuegoService(VideojuegoDAO videojuegoDAO){
        this.videojuegoDAO =videojuegoDAO;
    }
    public List<VideojuegoDTO> listarTodos() {
        return videojuegoDAO.listarTodos();
    }



    public List<VideojuegoDTO> buscarPorTitulo(String titulo, Integer idDesarrolladora, Integer anioDesde, Integer pegiMaximo, boolean soloDisponibles) {

        return videojuegoDAO.buscarPorTitulo(titulo, idDesarrolladora, anioDesde,pegiMaximo, soloDisponibles);
    }



}
