
package com.bibliotecadaniel.bibliotecadaniel.controller;

import com.bibliotecadaniel.bibliotecadaniel.service.SocioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.bibliotecadaniel.bibliotecadaniel.dto.NuevoSocioDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.format.annotation.DateTimeFormat;


@Controller
public class SocioController {

    private final SocioService socioService;

    public SocioController(SocioService socioService) {
        this.socioService = socioService;
    }


    @GetMapping("/socios")
    public String listarSocios(
            @RequestParam(required = false) String nombreApellidos,
            @RequestParam(required = false) String email,
            @RequestParam(required = false, defaultValue = "todos") String estado, Model model) {

        model.addAttribute("socios", socioService.buscarPorNombreApellidos(nombreApellidos, email, estado));

        model.addAttribute("nombreApellidos", nombreApellidos);
        model.addAttribute("email", email);
        model.addAttribute("estado", estado);

        return "socios";
    }

    @GetMapping("/socios/nuevo")
    public String mostrarFormularioNuevoSocio() {

        return "nuevo-socio";
    }


    @PostMapping("/socios/nuevo")
    public String registrarSocio(
            @ModelAttribute NuevoSocioDTO socio, Model model) {

        try {
            socioService.guardarSocio(socio);

            return "redirect:/socios";

        } catch (IllegalArgumentException e) {

            model.addAttribute("error", e.getMessage());

            model.addAttribute("socio", socio);

            return "nuevo-socio";
        }
    }


}

