package com.linking.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfig {

    @Bean
    public OpenAPI openAPI(@Value("v1.0") String appVersion) {
        Info info = new Info()
                .version(appVersion)
                .title("Linking API Docs")
                .description("Spring boot를 이용한 API Docs 입니다.");

        return new OpenAPI()
                .components(new Components())
                .info(info);

    }
}
