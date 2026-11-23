package com.bibliotecadaniel.bibliotecadaniel.controller.rest;

import com.bibliotecadaniel.bibliotecadaniel.dto.PrestamoRestDTO;
import com.bibliotecadaniel.bibliotecadaniel.service.PrestamoService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
public class PrestamoRestController {

    private final PrestamoService prestamoService;

    public PrestamoRestController(PrestamoService prestamoService) {
        this.prestamoService = prestamoService;
    }

    @GetMapping("/prestamos")
    public ResponseEntity<List<PrestamoRestDTO>> listarPrestamos(
            @RequestParam(required = false) String socio,
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) String estado,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fechaDesde) {

        List<PrestamoRestDTO> prestamos = prestamoService.listarPrestamosRest(socio, titulo, estado, fechaDesde);

        return ResponseEntity.ok(prestamos);
    }

    @GetMapping("/prestamos/{id}")
    public ResponseEntity<PrestamoRestDTO> buscarPrestamoPorId(@PathVariable int id) {

        PrestamoRestDTO prestamo = prestamoService.buscarPrestamoRestPorId(id);

        return ResponseEntity.ok(prestamo);
    }
}