package uk.co.caeldev.tweet.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.function.Predicate;

import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    @Bean
    public Docket tweetApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("tweet api")
                .select()
                .paths(PathSelectors.regex("/tweets.*"))
                .build()
                .pathMapping("/");
    }

    @Bean
    public Docket timelineApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("timeline api")
                .select()
                .paths(PathSelectors.regex("/timeline.*"))
                .build()
                .pathMapping("/");
    }

    @Bean
    public Docket followingApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("following api")
                .select()
                .paths(PathSelectors.regex("/following.*"))
                .build()
                .pathMapping("/");
    }
}
