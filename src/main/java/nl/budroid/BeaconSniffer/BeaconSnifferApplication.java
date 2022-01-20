package nl.budroid.BeaconSniffer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BeaconSnifferApplication {

	public static void main(String[] args) {
		System.setProperty("spring.jackson.serialization.INDENT_OUTPUT", "true");
		SpringApplication.run(BeaconSnifferApplication.class, args);
	}

}
