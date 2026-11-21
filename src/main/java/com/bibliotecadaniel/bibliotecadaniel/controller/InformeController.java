package com.bibliotecadaniel.bibliotecadaniel.controller;

import com.bibliotecadaniel.bibliotecadaniel.service.EstadisticasService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InformeController {

    private final EstadisticasService estadisticasService;

    public InformeController(EstadisticasService estadisticasService) {
        this.estadisticasService = estadisticasService;
    }

    @GetMapping("/informes/videojuegos-mas-prestados")
    public String mostrarVideojuegosMasPrestados(Model model) {
        model.addAttribute("videojuegos", estadisticasService.listarVideojuegosMasPrestados());
        return "informes/videojuegos-mas-prestados";
    }
}
