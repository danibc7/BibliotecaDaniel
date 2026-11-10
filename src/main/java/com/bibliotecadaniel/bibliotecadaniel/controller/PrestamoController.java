
package com.bibliotecadaniel.bibliotecadaniel.controller;

import com.bibliotecadaniel.bibliotecadaniel.service.PrestamoService;
import com.bibliotecadaniel.bibliotecadaniel.service.SocioService;
import com.bibliotecadaniel.bibliotecadaniel.service.CopiaService;
import com.bibliotecadaniel.bibliotecadaniel.dto.NuevoPrestamoDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import com.bibliotecadaniel.bibliotecadaniel.dto.ResumenPrestamoDTO;

@Controller
public class PrestamoController {

    private final PrestamoService prestamoService;
    private final SocioService socioService;
    private final CopiaService copiaService;

    public PrestamoController(PrestamoService prestamoService, SocioService socioService, CopiaService copiaService) {

        this.prestamoService = prestamoService;
        this.socioService = socioService;
        this.copiaService = copiaService;
    }

    @GetMapping("/prestamos/nuevo")
    public String mostrarFormulario(Model model) {

        model.addAttribute("socios", socioService.listarActivos());

        model.addAttribute("copias", copiaService.listarDisponibles());

        model.addAttribute("prestamo", new NuevoPrestamoDTO());

        return "prestamos/formulario";
    }

    @PostMapping("/prestamos/nuevo")
    public String registrarPrestamo(
            @ModelAttribute("prestamo") NuevoPrestamoDTO prestamo,
            Model model) {

        try {
            ResumenPrestamoDTO resumen = prestamoService.registrarPrestamo(prestamo);

            model.addAttribute("mensaje", "Préstamo registrado correctamente");

            model.addAttribute("resumen", resumen);

            model.addAttribute("prestamo", new NuevoPrestamoDTO());

        } catch (IllegalArgumentException e) {

            model.addAttribute("error", e.getMessage());
        }

        model.addAttribute("socios", socioService.listarActivos());

        model.addAttribute("copias", copiaService.listarDisponibles());

        return "prestamos/formulario";
    }

}
