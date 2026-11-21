package com.bibliotecadaniel.bibliotecadaniel.controller.rest;

import com.bibliotecadaniel.bibliotecadaniel.service.VideojuegoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.bibliotecadaniel.bibliotecadaniel.dto.VideojuegoDTO;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
@RestController
@RequestMapping("/api")
public class VideojuegoRestController {

    private final VideojuegoService videojuegoService;

    public VideojuegoRestController(VideojuegoService videojuegoService) {
        this.videojuegoService = videojuegoService;
    }
    @GetMapping("/videojuegos")
    public List<VideojuegoDTO> listarVideojuegos(
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) Integer idDesarrolladora,
            @RequestParam(required = false) Integer anioDesde,
            @RequestParam(required = false) Integer pegiMax,
            @RequestParam(defaultValue = "false") boolean soloDisponibles) {

        return videojuegoService.buscarPorTitulo(titulo, idDesarrolladora, anioDesde, pegiMax, soloDisponibles);
    }
    @GetMapping("/videojuegos/{id}")
    public ResponseEntity<VideojuegoDTO> obtenerVideojuego(@PathVariable int id) {

        VideojuegoDTO videojuego = videojuegoService.buscarPorId(id);

        if (videojuego == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(videojuego, HttpStatus.OK);
    }
    @PostMapping("/videojuegos")
    public ResponseEntity<VideojuegoDTO> crearVideojuego(@RequestBody VideojuegoDTO videojuego) {

        try {
            VideojuegoDTO videojuegoCreado = videojuegoService.crearVideojuego(videojuego);

            return new ResponseEntity<>(videojuegoCreado, HttpStatus.CREATED);

        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}