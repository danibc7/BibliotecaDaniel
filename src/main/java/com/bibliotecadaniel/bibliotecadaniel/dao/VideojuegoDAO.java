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
}
