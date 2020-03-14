package fr.arindam.aws.s3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class SpringS3FilehandlingApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringS3FilehandlingApplication.class, args);
	}
	
	@GetMapping("/")
	public String getHome() {
		return "Welcome to AWS S3 Application home page";
	}

}
