package car_wash.configuration;

import com.google.common.collect.Sets;
import io.swagger.annotations.Api;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Profile("dev")
@Configuration
@EnableSwagger2
public class SpringFoxConfig {

    @Bean
    ApiInfo metaData() {
        return new ApiInfoBuilder()
                .title("Swagger for Car Wash project")
                .description("OpenAPI for Car Wash test project")
                .version("1.0.0")
                .license("API Licence")
                .licenseUrl("http://localhost:8080")
                .build();
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .tags(
                        new Tag("Request", "Methods for requests")
                )
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .paths(PathSelectors.any())
                .build()
                .protocols(Sets.newHashSet("http", "https"))
                .apiInfo(metaData());
    }
}
