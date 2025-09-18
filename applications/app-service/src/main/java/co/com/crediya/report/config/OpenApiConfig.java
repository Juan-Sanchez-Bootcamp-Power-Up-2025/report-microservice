package co.com.crediya.report.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI crediYaOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("CrediYa - Report Microservice")
                        .version("v1")
                        .description("Report API"));
    }

}
