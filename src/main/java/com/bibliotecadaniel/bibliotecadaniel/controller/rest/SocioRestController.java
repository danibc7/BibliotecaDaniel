package com.bibliotecadaniel.bibliotecadaniel.controller.rest;

import com.bibliotecadaniel.bibliotecadaniel.dto.ActualizarSocioDTO;
import com.bibliotecadaniel.bibliotecadaniel.dto.SocioDTO;
import com.bibliotecadaniel.bibliotecadaniel.service.SocioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.bibliotecadaniel.bibliotecadaniel.dto.NuevoSocioDTO;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class SocioRestController {

    private final SocioService socioService;

    public SocioRestController(SocioService socioService) {
        this.socioService = socioService;
    }

    @GetMapping("/socios")
    public List<SocioDTO> listarSocios(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Boolean activo) {

        String estado;

        if (activo == null) {
            estado = "todos";
        } else if (activo) {
            estado = "activos";
        } else {
            estado = "inactivos";
        }

        return socioService.buscarPorNombreApellidos(nombre, email, estado);
    }

    @GetMapping("/socios/{id}")
    public ResponseEntity<SocioDTO> obtenerSocio(@PathVariable int id) {

        SocioDTO socio = socioService.buscarPorId(id);

        if (socio == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(socio, HttpStatus.OK);
    }
    @PostMapping("/socios")
    public ResponseEntity<SocioDTO> crearSocio(@RequestBody NuevoSocioDTO socio) {

        try {
            SocioDTO socioCreado = socioService.crearSocio(socio);

            return new ResponseEntity<>(socioCreado, HttpStatus.CREATED);

        } catch (IllegalArgumentException e) {

            if (e.getMessage().equals("Ya existe un socio registrado con ese correo electrónico")) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
            }

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
    @PutMapping("/socios/{id}")
    public ResponseEntity<SocioDTO> actualizarSocio(
            @PathVariable int id,
            @RequestBody ActualizarSocioDTO socio) {

        try {
            SocioDTO socioActualizado = socioService.actualizarSocio(id, socio);

            return ResponseEntity.ok(socioActualizado);

        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/socios/{id}")
    public ResponseEntity<Void> eliminarSocio(@PathVariable int id) {

        socioService.eliminarSocio(id);

        return ResponseEntity.noContent().build();
    }
}