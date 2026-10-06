package co.edu.uptc.servicio_web_calculadora.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uptc.servicio_web_calculadora.model.Persona;
import co.edu.uptc.servicio_web_calculadora.repository.PersonaRepository;

@Service
public class PersonaService {

    private static final int MAX_PAGE_SIZE = 100;

    private final PersonaRepository personaRepository;

    public PersonaService(PersonaRepository personaRepository) {
        this.personaRepository = personaRepository;
    }

    @Transactional(readOnly = true)
    public Slice<Persona> obtenerPersonas(Pageable pageable) {
        Pageable paginaLimitada = PageRequest.of(
                pageable.getPageNumber(),
                Math.min(pageable.getPageSize(), MAX_PAGE_SIZE)
        );
        return personaRepository.buscarTodas(paginaLimitada);
    }
}
