package com.bibliotecadaniel.bibliotecadaniel.controller;

import com.bibliotecadaniel.bibliotecadaniel.service.VideojuegoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.bibliotecadaniel.bibliotecadaniel.service.DesarrolladoraService;

@Controller
public class VideojuegoController {

    private final VideojuegoService videojuegoService;
    private final DesarrolladoraService desarrolladoraService;

    public VideojuegoController(VideojuegoService videojuegoService, DesarrolladoraService desarrolladoraService) {

        this.videojuegoService = videojuegoService;
        this.desarrolladoraService = desarrolladoraService;
    }




    @GetMapping("/videojuegos")
    public String listarVideojuegos(
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) Integer idDesarrolladora,
            @RequestParam(required = false) Integer anioDesde,
            @RequestParam(required = false) Integer pegiMaximo,
            @RequestParam(defaultValue = "false") boolean soloDisponibles,
            Model model) {

        model.addAttribute("videojuegos", videojuegoService.buscarPorTitulo(titulo, idDesarrolladora, anioDesde, pegiMaximo, soloDisponibles));

        model.addAttribute("titulo", titulo);

        model.addAttribute("desarrolladoras", desarrolladoraService.listarTodas());

        model.addAttribute("idDesarrolladora", idDesarrolladora);

        model.addAttribute("anioDesde", anioDesde);

        model.addAttribute("pegiMaximo", pegiMaximo);

        model.addAttribute("soloDisponibles", soloDisponibles);

        return "videojuegos";
    }


}
