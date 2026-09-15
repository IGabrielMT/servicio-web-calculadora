package co.edu.uptc.servicio_web_calculadora.dto;

public record OperacionResponseDTO(
        double num1,
        double num2,
        String operador,
        double resultado,
        String mensaje,
        String servidorProcesamiento // Nuevo campo
) {
    // Constructor secundario para no tener que pasar el nombre del servidor manualmente en el Controller
    public OperacionResponseDTO(double num1, double num2, String operador, double resultado, String mensaje) {
        this(
                num1,
                num2,
                operador,
                resultado,
                mensaje,
                System.getenv().getOrDefault("HOSTNAME", "Local/Desconocido")
        );
    }
}