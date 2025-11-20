package com.DigitalClassRoomManagement.Config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI digitalClassroomOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Digital Classroom Management API")
                        .description("API documentation for Digital Classroom Management project")
                        .version("v1.0")
                        .contact(new Contact()
                                .name("QuantumSoft — Digital Classroom Team")
                                .email("support@digitalclassroom.com")
                                .url("https://quntumsoft.digitalclassroom.com")
                        )
                )
                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                        )
                )
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
    }
}
