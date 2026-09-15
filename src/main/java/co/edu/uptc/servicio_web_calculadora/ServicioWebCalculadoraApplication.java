package co.edu.uptc.servicio_web_calculadora;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class ServicioWebCalculadoraApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServicioWebCalculadoraApplication.class, args);
	}

}
