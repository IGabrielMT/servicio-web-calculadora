package co.edu.uptc.servicio_web_calculadora.controller;

import co.edu.uptc.servicio_web_calculadora.model.Persona;
import co.edu.uptc.servicio_web_calculadora.repository.PersonaRepository;
import co.edu.uptc.servicio_web_calculadora.service.PersonaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    // Inyecta el valor definido en CONTENEDOR_ID del docker-compose.yml
    @Value("${CONTENEDOR_ID:desconocido}")
    private String contenedorId;

    @GetMapping("/stream")
    public ResponseEntity<StreamingResponseBody> obtenerPersonasStream(@RequestParam(defaultValue = "0") int limite) {
        StreamingResponseBody stream = personaService.obtenerPersonasStream(limite);
        return ResponseEntity.ok()
                .header("X-Contenedor-ID", contenedorId)
                .body(stream);
    }

    @GetMapping
    public ResponseEntity<Page<Persona>> listar(@RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "5") int size) {
        Page<Persona> resultado = personaRepository.findAll(PageRequest.of(page, size));
        return ResponseEntity.ok()
                .header("X-Contenedor-ID", contenedorId)
                .body(resultado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Persona> actualizar(@PathVariable Long id, @RequestBody Persona datos) {
        return personaRepository.findById(id)
                .map(persona -> {
                    persona.setNombre(datos.getNombre());
                    persona.setApellido(datos.getApellido());
                    return ResponseEntity.ok()
                            .header("X-Contenedor-ID", contenedorId)
                            .body(personaRepository.save(persona));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}