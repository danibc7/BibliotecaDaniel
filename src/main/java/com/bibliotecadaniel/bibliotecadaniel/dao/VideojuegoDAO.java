package com.bibliotecadaniel.bibliotecadaniel.dao;

import com.bibliotecadaniel.bibliotecadaniel.dto.VideojuegoDTO;
import com.bibliotecadaniel.bibliotecadaniel.repository.VideojuegoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class VideojuegoDAO {
    private final VideojuegoRepository videojuegoRepository;

    public VideojuegoDAO(VideojuegoRepository videojuegoRepository){
        this.videojuegoRepository = videojuegoRepository;
    }
    public List<VideojuegoDTO> listarTodos(){

        return videojuegoRepository.listarTodos();
    }



    public List<VideojuegoDTO> buscarPorTitulo(String titulo, Integer idDesarrolladora, Integer anioDesde, Integer pegiMaximo,boolean soloDisponibles) {

        return videojuegoRepository.buscarPorTitulo(titulo, idDesarrolladora, anioDesde, pegiMaximo, soloDisponibles);

    }
    public void guardarVideojuego(String titulo, int anioLanzamiento, int pegi, int idDesarrolladora){
        videojuegoRepository.guardarVideojuego(titulo,anioLanzamiento,pegi,idDesarrolladora);
    }
}
