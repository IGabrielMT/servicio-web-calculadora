package co.edu.uptc.servicio_web_calculadora.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.uptc.servicio_web_calculadora.model.Persona;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, Long> {
}