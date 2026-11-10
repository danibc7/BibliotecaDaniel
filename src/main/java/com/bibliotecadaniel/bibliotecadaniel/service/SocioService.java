
package com.bibliotecadaniel.bibliotecadaniel.service;

import com.bibliotecadaniel.bibliotecadaniel.dao.SocioDAO;
import com.bibliotecadaniel.bibliotecadaniel.dto.NuevoSocioDTO;
import com.bibliotecadaniel.bibliotecadaniel.dto.SocioDTO;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

import java.util.List;

@Service
public class SocioService {

    private final SocioDAO socioDAO;

    public SocioService(SocioDAO socioDAO) {
        this.socioDAO = socioDAO;
    }

    public List<SocioDTO> listarTodos() {
        return socioDAO.listarTodos();
    }

    public List<SocioDTO> buscarPorNombreApellidos(String nombreApellidos, String email, String estado) {

        return socioDAO.buscarPorNombreApellidos(nombreApellidos, email, estado);
    }

    public void guardarSocio(NuevoSocioDTO socio) {

        if (socio.getNombre() == null || socio.getNombre().isBlank() || socio.getNombre().length() > 40) {

            throw new IllegalArgumentException("El nombre no es válido");
        }

        if (socio.getApellidos() == null || socio.getApellidos().isBlank() || socio.getApellidos().length() > 80) {

            throw new IllegalArgumentException("Los apellidos no son válidos");
        }

        if (socio.getEmail() == null || !socio.getEmail().matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$") || socio.getEmail().length() > 100) {

            throw new IllegalArgumentException("El email no es válido");
        }

        if (socio.getFechaNacimiento() == null || !socio.getFechaNacimiento().isBefore(LocalDate.now())) {

            throw new IllegalArgumentException("La fecha de nacimiento no es válida");
        }

        if (socioDAO.existeEmail(socio.getEmail())) {

            throw new IllegalArgumentException("Ya existe un socio registrado con ese correo electrónico");
        }

        socioDAO.guardarSocio(socio);
    }


}
