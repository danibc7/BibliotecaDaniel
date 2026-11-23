
package com.bibliotecadaniel.bibliotecadaniel.dao;

import com.bibliotecadaniel.bibliotecadaniel.dto.SocioDTO;
import com.bibliotecadaniel.bibliotecadaniel.dto.NuevoSocioDTO;
import com.bibliotecadaniel.bibliotecadaniel.dto.ActualizarSocioDTO;

import java.util.List;

public interface ISocioDAO {

    List<SocioDTO> listarTodos();

    List<SocioDTO> buscarPorNombreApellidos(String nombreApellidos, String email, String estado);

    int guardarSocio(NuevoSocioDTO socio);

    boolean existeEmail(String email);

    List<SocioDTO> listarActivos();

    SocioDTO buscarPorId(int id);

    int actualizarSocio(int id, ActualizarSocioDTO socio);

    boolean tienePrestamos(int id);

    int eliminarSocio(int id);

}
