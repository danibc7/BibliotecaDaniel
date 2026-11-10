
package com.bibliotecadaniel.bibliotecadaniel.dao;

import com.bibliotecadaniel.bibliotecadaniel.dto.DesarrolladoraDTO;

import java.util.List;

public interface IDesarrolladoraDAO {

    List<DesarrolladoraDTO> listarTodas();

    boolean existePorId(int idDesarrolladora);

}
