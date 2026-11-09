
package com.bibliotecadaniel.bibliotecadaniel.controller;

import com.bibliotecadaniel.bibliotecadaniel.service.SocioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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



}

