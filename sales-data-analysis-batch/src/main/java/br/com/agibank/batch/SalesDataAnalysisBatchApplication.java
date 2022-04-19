package br.com.agibank.batch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing
public class SalesDataAnalysisBatchApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalesDataAnalysisBatchApplication.class, args);
	}

}
