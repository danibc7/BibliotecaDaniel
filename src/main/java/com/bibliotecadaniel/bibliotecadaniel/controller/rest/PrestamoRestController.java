package com.bibliotecadaniel.bibliotecadaniel.controller.rest;

import com.bibliotecadaniel.bibliotecadaniel.dto.NuevoPrestamoDTO;
import com.bibliotecadaniel.bibliotecadaniel.dto.PrestamoRestDTO;
import com.bibliotecadaniel.bibliotecadaniel.service.PrestamoService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
public class PrestamoRestController {

    private final PrestamoService prestamoService;

    public PrestamoRestController(PrestamoService prestamoService) {
        this.prestamoService = prestamoService;
    }

    @Operation(summary = "Listar préstamos",
            description = "Obtiene todos los préstamos y permite filtrarlos por socio, título, estado y fecha desde.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado de préstamos obtenido correctamente"),
            @ApiResponse(responseCode = "400", description = "Parámetros de búsqueda incorrectos")
    })
    @GetMapping("/prestamos")
    public ResponseEntity<List<PrestamoRestDTO>> listarPrestamos(
            @RequestParam(required = false) String socio,
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) String estado,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fechaDesde) {

        List<PrestamoRestDTO> prestamos = prestamoService.listarPrestamosRest(socio, titulo, estado, fechaDesde);

        return ResponseEntity.ok(prestamos);
    }

    @Operation(summary = "Obtener préstamo por ID",
            description = "Consulta los datos completos de un préstamo mediante su identificador."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Préstamo encontrado correctamente"),
            @ApiResponse(responseCode = "404", description = "El préstamo no existe")
    })
    @GetMapping("/prestamos/{id}")
    public ResponseEntity<PrestamoRestDTO> buscarPrestamoPorId(@PathVariable int id) {

        PrestamoRestDTO prestamo = prestamoService.buscarPrestamoRestPorId(id);

        return ResponseEntity.ok(prestamo);
    }
    @Operation(
            summary = "Registrar un préstamo",
            description = "Registra un préstamo comprobando que el socio y la copia existen, " +
                    "que se cumplen las cinco reglas de negocio y calculando " +
                    "la fecha límite de devolución."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Préstamo registrado correctamente"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Faltan datos obligatorios o la petición es incorrecta"),
            @ApiResponse(
                    responseCode = "404",
                    description = "El socio o la copia no existen"),
            @ApiResponse(
                    responseCode = "422",
                    description = "No se cumple alguna de las reglas de negocio RN-1 a RN-5")
    })
    @PostMapping("/prestamos")
    public ResponseEntity<PrestamoRestDTO> registrarPrestamo(@RequestBody NuevoPrestamoDTO prestamo) {

        PrestamoRestDTO prestamoCreado = prestamoService.registrarPrestamoRest(prestamo);

        return new ResponseEntity<>(prestamoCreado, HttpStatus.CREATED);
    }
    @Operation(
            summary = "Devolver un préstamo",
            description = "Registra la devolución de un préstamo, calcula la multa " +
                    "por retraso y permite indicar si la copia está dañada. " +
                    "Si está dañada, se marca como DAÑADO e inactiva."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Préstamo devuelto correctamente"),
            @ApiResponse(
                    responseCode = "404",
                    description = "El préstamo no existe"),
            @ApiResponse(
                    responseCode = "409",
                    description = "El préstamo ya ha sido devuelto")
    })
    @PutMapping("/prestamos/{id}/devolucion")
    public ResponseEntity<PrestamoRestDTO> devolverPrestamo(
            @PathVariable int id,
            @RequestParam(defaultValue = "false") boolean copiaDanada) {

        PrestamoRestDTO prestamoDevuelto = prestamoService.devolverPrestamoRest(id, copiaDanada);

        return ResponseEntity.ok(prestamoDevuelto);
    }
}