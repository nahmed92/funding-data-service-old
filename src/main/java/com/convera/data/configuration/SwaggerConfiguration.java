package com.convera.data.configuration;



import javax.annotation.PostConstruct;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import io.swagger.v3.oas.models.media.DateTimeSchema;
import org.springdoc.core.SpringDocUtils;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {
	@PostConstruct
	public void localDateTimeSchemaConfig() {
		SpringDocUtils.getConfig().replaceWithSchema(LocalDateTime.class, new DateTimeSchema()
				.format("date-time")
				.example(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'"))));
	}

}
