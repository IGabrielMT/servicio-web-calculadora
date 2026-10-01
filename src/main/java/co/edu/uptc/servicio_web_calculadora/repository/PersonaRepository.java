package co.edu.uptc.servicio_web_calculadora.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import co.edu.uptc.servicio_web_calculadora.model.Persona;

import java.util.stream.Stream;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, Long> {
    @Query("SELECT p FROM Persona p")
    Stream<Persona> obtenerTodasStream();
}