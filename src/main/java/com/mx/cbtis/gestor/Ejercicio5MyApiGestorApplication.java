package com.mx.cbtis.gestor;

import java.net.URI;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Ejercicio5MyApiGestorApplication {

	public static void main(String[] args) {
		configureRenderDatabaseUrl();
		SpringApplication.run(Ejercicio5MyApiGestorApplication.class, args);
	}

	private static void configureRenderDatabaseUrl() {
		String dbHost = System.getenv("DB_HOST");
		String rawUrl = System.getenv("DB_URL");

		if (rawUrl != null && (rawUrl.startsWith("postgresql://") || rawUrl.startsWith("postgres://"))) {
			URI uri = URI.create(rawUrl);
			int port = uri.getPort() > 0 ? uri.getPort() : 5432;
			String jdbcUrl = "jdbc:postgresql://" + uri.getHost() + ":" + port + uri.getPath();
			System.setProperty("spring.datasource.url", jdbcUrl);
			if (uri.getUserInfo() != null && uri.getUserInfo().contains(":")) {
				String[] creds = uri.getUserInfo().split(":", 2);
				System.setProperty("spring.datasource.username", creds[0]);
				System.setProperty("spring.datasource.password", creds[1]);
			}
		} else if (dbHost != null && !dbHost.isBlank()) {
			String dbPort = System.getenv("DB_PORT");
			String dbName = System.getenv("DB_NAME");
			String portPart = (dbPort != null && !dbPort.isBlank()) ? dbPort : "5432";
			String jdbcUrl = "jdbc:postgresql://" + dbHost + ":" + portPart + "/" + (dbName != null ? dbName : "dbgestor");
			System.setProperty("spring.datasource.url", jdbcUrl);
		}
	}

}
