package com.bibliotecadaniel.bibliotecadaniel.controller;

import com.bibliotecadaniel.bibliotecadaniel.service.VideojuegoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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


    @GetMapping("/videojuegos/nuevo")
    public String mostrarFormularioNuevo(Model model) {

        model.addAttribute("desarrolladoras", desarrolladoraService.listarTodas());

        return "nuevo-videojuego";
    }

    @PostMapping("/videojuegos/nuevo")
    public String guardarVideojuego(
            @RequestParam String titulo,
            @RequestParam int anioLanzamiento,
            @RequestParam int pegi,
            @RequestParam int idDesarrolladora,
            Model model) {

        try {

            videojuegoService.guardarVideojuego(titulo, anioLanzamiento, pegi, idDesarrolladora);

            return "redirect:/videojuegos";

        } catch (IllegalArgumentException e) {

            model.addAttribute("error", e.getMessage());

            model.addAttribute("titulo", titulo);
            model.addAttribute("anioLanzamiento", anioLanzamiento);
            model.addAttribute("pegi", pegi);
            model.addAttribute("idDesarrolladora", idDesarrolladora);

            model.addAttribute("desarrolladoras", desarrolladoraService.listarTodas());

            return "nuevo-videojuego";
        }

    }




}
