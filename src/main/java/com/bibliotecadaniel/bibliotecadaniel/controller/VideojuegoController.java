package com.bibliotecadaniel.bibliotecadaniel.controller;

import com.bibliotecadaniel.bibliotecadaniel.service.VideojuegoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class VideojuegoController {

    private final VideojuegoService videojuegoService;
    public VideojuegoController(VideojuegoService videojuegoService){
        this.videojuegoService = videojuegoService;
    }

    @GetMapping("/videojuegos")
    public String listarVideojuegos(Model model){
        model.addAttribute("videojuegos",videojuegoService.listarTodos());
        return "videojuegos";
    }
}
