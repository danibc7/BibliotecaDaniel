package com.bibliotecadaniel.bibliotecadaniel.controller.rest;

import com.bibliotecadaniel.bibliotecadaniel.dto.EstadisticasDTO;
import com.bibliotecadaniel.bibliotecadaniel.service.EstadisticasService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class EstadisticasRestController {

    private final EstadisticasService estadisticasService;

    public EstadisticasRestController(EstadisticasService estadisticasService) {
        this.estadisticasService = estadisticasService;
    }

    @GetMapping("/estadisticas")
    public ResponseEntity<Map<String, Integer>> obtenerEstadisticas() {

        EstadisticasDTO datos = estadisticasService.obtenerEstadisticas();

        Map<String, Integer> respuesta = new LinkedHashMap<>();

        respuesta.put("videojuegos", datos.getTotalVideojuegos());
        respuesta.put("sociosActivos", datos.getSociosActivos());
        respuesta.put("prestamosAbiertos", datos.getPrestamosAbiertos());
        respuesta.put("prestamosVencidos", datos.getPrestamosVencidos());

        return ResponseEntity.ok(respuesta);
    }
}
