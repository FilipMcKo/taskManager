package meelogic.filip.taskManager;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }
    /** NOTES:
     * After the Docket bean is defined, its select() method returns an instance
     * of ApiSelectorBuilder, which provides a way to control the endpoints exposed by Swagger.
     *
     * This configuration is enough to integrate Swagger 2 into an existing Spring Boot project.
     * For other Spring projects, some additional tuning is required.
     *
     * Documentation in JSON format is avalible: http://localhost:8080/v2/api-docs
     * Documentation in UI format is avalible:  http://localhost:8080/swagger-ui.html
     */
}
