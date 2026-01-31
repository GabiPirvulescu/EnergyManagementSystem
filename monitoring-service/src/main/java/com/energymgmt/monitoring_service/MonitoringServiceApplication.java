package com.energymgmt.monitoring_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan; // <-- IMPORT
import org.springframework.data.jpa.repository.config.EnableJpaRepositories; // <-- IMPORT

@SpringBootApplication
@EntityScan(basePackages = "com.energymgmt.monitoring_service.entities")
@EnableJpaRepositories(basePackages = "com.energymgmt.monitoring_service.repository")
public class MonitoringServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MonitoringServiceApplication.class, args);
	}

}