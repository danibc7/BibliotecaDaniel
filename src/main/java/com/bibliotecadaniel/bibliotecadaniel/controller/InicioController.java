package com.bibliotecadaniel.bibliotecadaniel.controller;

import com.bibliotecadaniel.bibliotecadaniel.dto.EstadisticasDTO;
import com.bibliotecadaniel.bibliotecadaniel.service.EstadisticasService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InicioController {

    private final EstadisticasService estadisticasService;

    public InicioController(EstadisticasService estadisticasService) {
        this.estadisticasService = estadisticasService;
    }

    @GetMapping("/")
    public String mostrarInicio(Model model) {

        EstadisticasDTO estadisticas = estadisticasService.obtenerEstadisticas();

        model.addAttribute("estadisticas", estadisticas);

        return "index";
    }
}

