
package com.bibliotecadaniel.bibliotecadaniel.dao;

import com.bibliotecadaniel.bibliotecadaniel.dto.VideojuegoDTO;

import java.util.List;

public interface IVideojuegoDAO {

    List<VideojuegoDTO> listarTodos();

    List<VideojuegoDTO> buscarPorTitulo(String titulo, Integer idDesarrolladora, Integer anioDesde, Integer pegiMaximo, boolean soloDisponibles);

    int guardarVideojuego(String titulo, int anioLanzamiento, int pegi, int idDesarrolladora);

    VideojuegoDTO buscarPorId(int id);
}
