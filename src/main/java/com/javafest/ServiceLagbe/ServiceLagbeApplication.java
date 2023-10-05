package com.javafest.ServiceLagbe;

import com.twilio.Twilio;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ServiceLagbeApplication {
	public static void main(String[] args) {
		SpringApplication.run(ServiceLagbeApplication.class, args);
	}

}
