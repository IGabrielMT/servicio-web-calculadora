package co.edu.uptc.servicio_web_calculadora.controller;

import co.edu.uptc.servicio_web_calculadora.dto.RespuestaWrapper;
import co.edu.uptc.servicio_web_calculadora.model.Persona;
import co.edu.uptc.servicio_web_calculadora.repository.PersonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/personas")
public class PersonaController {

    @Autowired
    private PersonaRepository personaRepository;

    @Value("${CONTENEDOR_ID:desconocido}")
    private String contenedorId;

    @GetMapping
    public ResponseEntity<RespuestaWrapper<Page<Persona>>> listar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        Page<Persona> resultado = personaRepository.findAll(
                PageRequest.of(page, size, Sort.by("id").ascending())
        );

        return ResponseEntity.ok(new RespuestaWrapper<>(contenedorId, resultado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RespuestaWrapper<Persona>> actualizar(@PathVariable Long id, @RequestBody Persona datos) {
        return personaRepository.findById(id)
                .map(persona -> {
                    persona.setNombre(datos.getNombre());
                    persona.setApellido(datos.getApellido());
                    Persona guardada = personaRepository.save(persona);
                    return ResponseEntity.ok(new RespuestaWrapper<>(contenedorId, guardada));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}