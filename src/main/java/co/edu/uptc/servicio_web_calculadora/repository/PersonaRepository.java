package co.edu.uptc.servicio_web_calculadora.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import co.edu.uptc.servicio_web_calculadora.model.Persona;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, Long> {

    @Query("SELECT p FROM Persona p ORDER BY p.id ASC")
    Slice<Persona> buscarTodas(Pageable pageable);
}