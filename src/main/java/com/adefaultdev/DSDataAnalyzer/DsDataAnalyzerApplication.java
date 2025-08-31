package com.adefaultdev.DSDataAnalyzer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the DS Data Analyzer Spring Boot application.
 *
 * <p>Configures and bootstraps the Spring application with auto-configuration,
 * component scanning, and embedded server support.</p>
 *
 * @author ADefaultDev
 * @since 1.0
 */
@SpringBootApplication
public class DsDataAnalyzerApplication {

	/**
	 * Main method that boots the Spring application context.
	 * Starts embedded server and loads all configured beans.
	 *
	 * @param args command line arguments (e.g., --server.port=8081)
	 */
	public static void main(String[] args) {
		SpringApplication.run(DsDataAnalyzerApplication.class, args);
	}

}
