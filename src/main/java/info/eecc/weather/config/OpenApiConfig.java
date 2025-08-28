package info.eecc.weather.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenApiConfig {

	@Bean
	public OpenAPI weatherOpenAPI() {
		Server localServer = new Server()
				.url("http://localhost:8080")
				.description("Local development server");

		Contact contact = new Contact()
				.email("admin@eecc.info")
				.url("https://eecc.info");

		License license = new License()
				.name("Apache 2.0")
				.url("https://www.apache.org/licenses/LICENSE-2.0");

		Info info = new Info()
				.title("Weather API")
				.version("1.0.0")
				.description(
						"A comprehensive weather API that provides current weather information by coordinates or city name. "
								+
								"This API serves as a wrapper for meteorological data services.\n\n"
								+
								"**Data Sources & Attribution:**\n" +
								"- Weather data provided by [Open-Meteo.com](https://open-meteo.com/) under CC BY 4.0 license\n"
								+
								"- Geocoding data provided by OpenStreetMap contributors under ODbL license\n\n"
								+
								"**Compliance Notice:**\n" +
								"This service uses Open-Meteo API data in accordance with the Attribution 4.0 International (CC BY 4.0) license. "
								+
								"Weather data by [Open-Meteo.com](https://open-meteo.com/). "
								+
								"Geocoding services utilize OpenStreetMap data © OpenStreetMap contributors, available under the Open Database License (ODbL).")
				.contact(contact)
				.license(license);

		return new OpenAPI()
				.info(info)
				.servers(List.of(localServer));
	}
}
