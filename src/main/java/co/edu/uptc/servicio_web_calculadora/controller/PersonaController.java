package co.edu.uptc.servicio_web_calculadora.controller;

import co.edu.uptc.servicio_web_calculadora.model.Persona;
import co.edu.uptc.servicio_web_calculadora.repository.PersonaRepository;
import co.edu.uptc.servicio_web_calculadora.service.PersonaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@RestController
@RequestMapping("/api/personas")
public class PersonaController {

    @Autowired
    private PersonaService personaService;

    @Autowired
    private PersonaRepository personaRepository;

    // Endpoint para streaming de datos usando PersonaService
    // Ejemplo: GET http://192.168.56.101/api/personas/stream?limite=100
    @GetMapping("/stream")
    public StreamingResponseBody obtenerPersonasStream(@RequestParam(defaultValue = "0") int limite) {
        return personaService.obtenerPersonasStream(limite);
    }

    // Endpoint paginado estándar para consultas rápidas
    // Ejemplo: GET http://192.168.56.101/api/personas?page=0&size=5
    @GetMapping
    public Page<Persona> listar(@RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "5") int size) {
        return personaRepository.findAll(PageRequest.of(page, size));
    }

    // Endpoint para actualizar registros
    @PutMapping("/{id}")
    public ResponseEntity<Persona> actualizar(@PathVariable Long id, @RequestBody Persona datos) {
        return personaRepository.findById(id)
                .map(persona -> {
                    persona.setNombre(datos.getNombre());
                    persona.setApellido(datos.getApellido());
                    return ResponseEntity.ok(personaRepository.save(persona));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}