package co.edu.uptc.servicio_web_calculadora.controller;

import co.edu.uptc.servicio_web_calculadora.model.Persona;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import co.edu.uptc.servicio_web_calculadora.dto.OperacionResponseDTO;
import co.edu.uptc.servicio_web_calculadora.service.CalculadoraService;
import co.edu.uptc.servicio_web_calculadora.service.PersonaService;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class CalculadoraController {

    private final CalculadoraService calculadoraService;
    @Getter
    private final PersonaService personaService;
    private final String RUTA_CSV = "/app/datos/personas.csv";

    public CalculadoraController(CalculadoraService calculadoraService, PersonaService personaService) {
        this.calculadoraService = calculadoraService;
        this.personaService = personaService;
    }

    @GetMapping("/calcular")
    public ResponseEntity<OperacionResponseDTO> procesarCalculo(
            @RequestParam double num1,
            @RequestParam double num2,
            @RequestParam String operador) {

        double resultado = calculadoraService.realizarOperacion(num1, num2, operador);
        String mensaje = "El resultado de la " + operador + " entre " + num1 + " y " + num2 + " es: " + resultado;

        return ResponseEntity.ok(new OperacionResponseDTO(num1, num2, operador, resultado, mensaje));
    }

    @GetMapping("/personas")
    public ResponseEntity<Map<String, Object>> obtenerPersonasPaginadas(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {

        List<Persona> personas = new ArrayList<>();
        long lineasSaltar = (long) page * size;

        try (BufferedReader br = new BufferedReader(new FileReader(RUTA_CSV))) {
            br.readLine(); // Saltar encabezado

            // Saltar hasta la página solicitada
            for (long i = 0; i < lineasSaltar; i++) {
                if (br.readLine() == null) break;
            }

            // Leer los registros de esta página
            String linea;
            int leidos = 0;
            while ((linea = br.readLine()) != null && leidos < size) {
                if (linea.trim().isEmpty()) continue;
                String[] datos = linea.split(",");
                if (datos.length >= 3) {
                    personas.add(new Persona(datos[0].trim(), datos[1].trim(), datos[2].trim()));
                    leidos++;
                }
            }
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }

        // Construir la respuesta con el ID del contenedor que atendió la petición
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("contenedor_id", System.getenv().getOrDefault("HOSTNAME", "Desconocido"));
        respuesta.put("pagina_actual", page);
        respuesta.put("tamano_pagina", size);
        respuesta.put("datos", personas);

        return ResponseEntity.ok(respuesta);
    }

    // Endpoint PUT: Modificar un registro existente en el CSV
    @PutMapping("/personas/{id}")
    public ResponseEntity<Map<String, Object>> modificarPersona(
            @PathVariable String id,
            @RequestBody Persona personaActualizada) {

        File archivoOriginal = new File(RUTA_CSV);
        File archivoTemporal = new File(RUTA_CSV + ".tmp_" + System.currentTimeMillis());
        boolean encontrado = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(archivoOriginal));
             BufferedWriter writer = new BufferedWriter(new FileWriter(archivoTemporal))) {

            String linea;
            while ((linea = reader.readLine()) != null) {
                // Validar si la línea actual es la del ID que buscamos
                // Comparamos usando "id," para no confundir el ID 1 con el 10 o el 100
                if (linea.startsWith(id + ",")) {
                    // Escribimos la línea con los datos nuevos
                    String nuevaLinea = String.format("%s,%s,%s",
                            id, personaActualizada.getNombre(), personaActualizada.getApellido());
                    writer.write(nuevaLinea + "\n");
                    encontrado = true;
                } else {
                    // Si no es, copiamos la línea original tal cual
                    writer.write(linea + "\n");
                }
            }

        } catch (IOException e) {
            archivoTemporal.delete(); // Limpiar si hay error
            return ResponseEntity.internalServerError().build();
        }

        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("contenedor_id", System.getenv().getOrDefault("HOSTNAME", "Desconocido"));

        if (encontrado) {
            try {
                Files.move(archivoTemporal.toPath(), archivoOriginal.toPath(), StandardCopyOption.REPLACE_EXISTING);
                respuesta.put("mensaje", "Registro " + id + " modificado exitosamente");
                return ResponseEntity.ok(respuesta);
            } catch (IOException e) {
                return ResponseEntity.internalServerError().build();
            }
        } else {
            archivoTemporal.delete();
            respuesta.put("mensaje", "Registro " + id + " no encontrado");
            return ResponseEntity.status(404).body(respuesta);
        }
    }

}
