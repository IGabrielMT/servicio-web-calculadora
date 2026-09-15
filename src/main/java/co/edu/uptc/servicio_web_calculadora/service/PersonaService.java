package co.edu.uptc.servicio_web_calculadora.service;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import co.edu.uptc.servicio_web_calculadora.model.Persona;
import co.edu.uptc.servicio_web_calculadora.repository.PersonaRepository;

@Service
public class PersonaService {

    private final PersonaRepository personaRepository;

    public PersonaService(PersonaRepository personaRepository) {
        this.personaRepository = personaRepository;
    }

    public StreamingResponseBody obtenerPersonasStream(int limite) {
        return outputStream -> {
            try (Stream<Persona> stream = personaRepository.obtenerTodasStream()) {
                outputStream.write("[\n".getBytes(StandardCharsets.UTF_8));

                AtomicBoolean esPrimeraLinea = new AtomicBoolean(true);
                Stream<Persona> streamFinal = stream;

                if (limite > 0) {
                    streamFinal = streamFinal.limit(limite);
                }

                streamFinal.forEach(persona -> {
                    try {
                        if (!esPrimeraLinea.get()) {
                            outputStream.write(",\n".getBytes(StandardCharsets.UTF_8));
                        } else {
                            esPrimeraLinea.set(false);
                        }

                        String json = String.format(
                                "{\"id\":\"%s\",\"nombre\":\"%s\",\"apellido\":\"%s\"}",
                                persona.getId(), persona.getNombre(), persona.getApellido()
                        );
                        outputStream.write(json.getBytes(StandardCharsets.UTF_8));
                    } catch (Exception e) {
                        throw new RuntimeException("Error escribiendo stream", e);
                    }
                });

                outputStream.write("\n]".getBytes(StandardCharsets.UTF_8));
                outputStream.flush();
            } catch (Exception e) {
                System.err.println("Error procesando el archivo masivo: " + e.getMessage());
            }
        };
    }
}