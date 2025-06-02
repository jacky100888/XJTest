package com.example.demo.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        servers = {
                @Server(url = "/", description = "Default Server URL")
        }
)
public class OpenApiConfig {

    @Bean
    public OpenAPI orderManagementOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("訂單管理系統 API")
                        .description("提供會員管理、產品管理和訂單處理功能的Restful API")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("開發團隊")
                                .email("dev@example.com")
                                .url("https://example.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")));
    }
}