
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.bibliotecadaniel.bibliotecadaniel.dto.ResumenDevolucionDTO;


import java.math.BigDecimal;
import java.time.LocalDate;

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

    @GetMapping("/prestamos")
    public String listarPrestamos(
            @RequestParam(required = false) String socio,
            @RequestParam(required = false) String videojuego,
            @RequestParam(required = false) String estado,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fechaDesde,
            Model model) {

        model.addAttribute("prestamos", prestamoService.listarPrestamos(socio, videojuego, estado, fechaDesde));

        model.addAttribute("socio", socio);
        model.addAttribute("videojuego", videojuego);
        model.addAttribute("estado", estado);
        model.addAttribute("fechaDesde", fechaDesde);

        return "prestamos/listado";
    }

    @GetMapping("/prestamos/devolver")
    public String mostrarPrestamosParaDevolver(@RequestParam(required = false) String socio, Model model) {

        model.addAttribute("prestamos", prestamoService.listarPrestamosAbiertos(socio));

        model.addAttribute("socio", socio);

        return "prestamos/devolver";
    }

    @PostMapping("/prestamos/devolver")
    public String procesarDevolucion(
            @RequestParam int idPrestamo,
            @RequestParam(defaultValue = "false") boolean copiaDanada,
            RedirectAttributes redirectAttributes) {

        try {
            prestamoService.devolverPrestamo(idPrestamo, copiaDanada);

            ResumenDevolucionDTO resumen = prestamoService.obtenerResumenDevolucion(idPrestamo);

            redirectAttributes.addFlashAttribute("resumenDevolucion", resumen);

            redirectAttributes.addFlashAttribute("copiaDanada", copiaDanada);

        } catch (IllegalArgumentException | IllegalStateException e) {

            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/prestamos/devolver";
    }

}
