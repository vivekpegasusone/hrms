package com.whizzy.hrms.core.config.openapi;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    private static final String APP_SERVER = "http://localhost:8080";
    private static final String DESCRIPTION = "Localhost Server URL";

    @Bean
    public OpenAPI openApiInformation() {
        Server localServer = new Server().url(APP_SERVER).description(DESCRIPTION);
        Contact contact = new Contact().email("vivek@gmail.com").name("Brijeshwar Singh");
        Info info = new Info().contact(contact).description("Whizzy HRMS API")
                .summary("HRMS application API").title("Whizzy HRMS").version("V1.0.0");

        return new OpenAPI().info(info).addServersItem(localServer);
    }
}
