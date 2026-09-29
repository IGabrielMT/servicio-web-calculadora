package co.edu.uptc.servicio_web_calculadora.controller;

import co.edu.uptc.servicio_web_calculadora.model.Persona;
import co.edu.uptc.servicio_web_calculadora.repository.PersonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/personas")
public class PersonaController {

    @Autowired
    private PersonaRepository personaRepository;

    @GetMapping
    public Page<Persona> listar(@RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "5") int size) {
        return personaRepository.findAll(PageRequest.of(page, size));
    }

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

