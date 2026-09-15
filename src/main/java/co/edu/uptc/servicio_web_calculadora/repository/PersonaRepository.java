package co.edu.uptc.servicio_web_calculadora.repository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.springframework.stereotype.Repository;

import co.edu.uptc.servicio_web_calculadora.model.Persona;

@Repository
public class PersonaRepository {

    private String obtenerRutaArchivo() {
        File archivoContainer = new File("/app/datos/personas.csv");
        if (archivoContainer.exists()) {
            return "/app/datos/personas.csv";
        }
        return "personas.csv";
    }

    public Stream<Persona> obtenerTodasStream() throws IOException {
        String ruta = obtenerRutaArchivo();
        File archivo = new File(ruta);

        if (!archivo.exists()) {
            System.err.println("El archivo CSV no existe en la ruta: " + ruta);
            return Stream.empty();
        }

        // Files.lines lee linea por linea sin cargar los 200MB en la RAM
        return Files.lines(Paths.get(ruta))
                .filter(linea -> !linea.trim().isEmpty())
                .filter(linea -> !(linea.toLowerCase().contains("id") && linea.toLowerCase().contains("nombre")))
                .map(linea -> {
                    String[] datos = linea.split(",");
                    if (datos.length >= 3) {
                        return new Persona(datos[0].trim(), datos[1].trim(), datos[2].trim());
                    }
                    return null;
                })
                .filter(persona -> persona != null);
    }
}