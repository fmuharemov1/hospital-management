package ba.unsa.etf.hospital;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages = "ba.unsa.etf.hospital.client")
@SpringBootApplication
public class HospitalFinanceApplication {

	public static void main(String[] args) {
		SpringApplication.run(HospitalFinanceApplication.class, args);
	}

}
