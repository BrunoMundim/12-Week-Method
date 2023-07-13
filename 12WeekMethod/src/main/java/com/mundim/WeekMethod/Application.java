package com.mundim.WeekMethod;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@SecurityScheme(name = "jwt", scheme = "bearer", type = SecuritySchemeType.HTTP, in = SecuritySchemeIn.HEADER)
@OpenAPIDefinition(
		info = @Info(
				title = "12 Week Method API",
				version = "1.0.0",
				description = "Bem-vindo à documentação da API do Goal Sprinter! Esta documentação fornece informações detalhadas sobre os endpoints, parâmetros, solicitações e respostas da API para ajudá-lo a integrar e utilizar os recursos do 12 Week Method em seu aplicativo.",
				license = @License(name = "MIT License"),
				contact = @Contact(name = "Bruno Mundim", email = "brunomundimfranco@gmail.com")
		)
)
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
