package com.bibliotecadaniel.bibliotecadaniel.controller.rest;

import com.bibliotecadaniel.bibliotecadaniel.dto.DesarrolladoraDTO;
import com.bibliotecadaniel.bibliotecadaniel.service.DesarrolladoraService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class DesarrolladoraRestController {

    private final DesarrolladoraService desarrolladoraService;

    public DesarrolladoraRestController(DesarrolladoraService desarrolladoraService) {
        this.desarrolladoraService = desarrolladoraService;
    }

    @GetMapping("/desarrolladoras")
    public ResponseEntity<List<DesarrolladoraDTO>> listarDesarrolladoras() {

        List<DesarrolladoraDTO> desarrolladoras = desarrolladoraService.listarTodas();

        return ResponseEntity.ok(desarrolladoras);
    }
}