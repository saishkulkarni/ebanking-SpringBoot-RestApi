package org.jsp.ebanking.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
@Controller
public class SwaggerConfig {

	@GetMapping("/")
	public String loadSwagger() {
		return "redirect:swagger-ui/index.html";
	}

	@Bean
	OpenAPI ebankingOpenAPI() {
		return new OpenAPI()
				.info(new Info().title("E-Banking REST API")
						.description("API documentation for the E-Banking Internet Banking System").version("1.0.0")
						.contact(new Contact().name("E-Banking Support Team").email("support@ebanking.com")
								.url("https://www.ebanking.com"))
						.license(new License().name("Apache 2.0")
								.url("https://www.apache.org/licenses/LICENSE-2.0.html")))
				.servers(List.of(new Server().url("http://localhost:8055").description("Local Development Server"),
						new Server().url("https://api.ebanking.com").description("Production Server")));
	}

}