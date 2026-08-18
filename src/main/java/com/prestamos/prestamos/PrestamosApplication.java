package com.prestamos.prestamos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase de entrada (entry point) de la aplicación Spring Boot.
 *
 * <p>La anotación {@link SpringBootApplication} habilita la configuración
 * automática, el escaneo de componentes y el registro de propiedades
 * definidas bajo el prefijo {@code spring.*}.</p>
 */
@SpringBootApplication
public class PrestamosApplication {

	/**
	 * Arranca el contexto de Spring y deja la aplicación escuchando
	 * peticiones HTTP en el puerto configurado.
	 *
	 * @param args argumentos de línea de comandos (opcionalmente para
	 *             sobrescribir propiedades como {@code --server.port}).
	 */
	public static void main(String[] args) {
		SpringApplication.run(PrestamosApplication.class, args);
	}

}
