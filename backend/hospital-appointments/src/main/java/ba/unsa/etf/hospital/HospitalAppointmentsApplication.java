package ba.unsa.etf.hospital;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;


@SpringBootApplication(scanBasePackages = {
		"ba.unsa.etf.hospital",      // Glavni paket aplikacije
		"com.example.logging"        // Paket za logging (npr. gRPC klijent)
})
public class HospitalAppointmentsApplication {

	public static void main(String[] args) {
		SpringApplication.run(HospitalAppointmentsApplication.class, args);
	}

	/**
	 * Kreira RestTemplate bean sa LoadBalanced anotacijom.
	 * Omogućava da RestTemplate koristi Eureka service discovery za komunikaciju između mikroservisa.
	 */
	@Bean
	@LoadBalanced
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}
}
