package co.edu.uptc.servicio_web_calculadora.controller;

import co.edu.uptc.servicio_web_calculadora.dto.RespuestaWrapper;
import co.edu.uptc.servicio_web_calculadora.model.Persona;
import co.edu.uptc.servicio_web_calculadora.repository.PersonaRepository;
import co.edu.uptc.servicio_web_calculadora.service.PersonaService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/personas")
public class PersonaController {

    private final PersonaRepository personaRepository;
    private final PersonaService personaService;

    @Value("${CONTENEDOR_ID:desconocido}")
    private String contenedorId;

    public PersonaController(PersonaRepository personaRepository, PersonaService personaService) {
        this.personaRepository = personaRepository;
        this.personaService = personaService;
    }

    @GetMapping
    public ResponseEntity<RespuestaWrapper<RespuestaWrapper<Slice<Persona>>>> listar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Slice<Persona> resultado = personaService.obtenerPersonas(
                    PageRequest.of(page, size)
        );
        RespuestaWrapper<Slice<Persona>> nivelInterno = new RespuestaWrapper<>(contenedorId, resultado);
        RespuestaWrapper<RespuestaWrapper<Slice<Persona>>> nivelExterno = new RespuestaWrapper<>("Titulo:11111", nivelInterno);
        return ResponseEntity.ok(nivelExterno);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RespuestaWrapper<RespuestaWrapper<Persona>>> actualizar(@PathVariable Long id, @RequestBody Persona datos) {
        return personaRepository.findById(id)
                .map(persona -> {
                    persona.setNombre(datos.getNombre());
                    persona.setApellido(datos.getApellido());
                    Persona guardada = personaRepository.save(persona);

                    RespuestaWrapper<Persona> nivelInterno = new RespuestaWrapper<>(contenedorId, guardada);

                    RespuestaWrapper<RespuestaWrapper<Persona>> nivelExterno = new RespuestaWrapper<>("Titulo:11111", nivelInterno);

                    return ResponseEntity.ok(nivelExterno);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}