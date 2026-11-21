
package com.bibliotecadaniel.bibliotecadaniel.dao;

import com.bibliotecadaniel.bibliotecadaniel.dto.EstadisticasDTO;
import com.bibliotecadaniel.bibliotecadaniel.dto.VideojuegoMasPrestadoDTO;
import java.util.List;

public interface IEstadisticasDAO {

    EstadisticasDTO obtenerEstadisticas();

    List<VideojuegoMasPrestadoDTO> listarVideojuegosMasPrestados();

}
