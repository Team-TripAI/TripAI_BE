package com.milkcow.tripai.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

@Configuration
@EnableWebMvc
public class SwaggerConfig {
    private ApiInfo swaggerInfo(){
        return new ApiInfoBuilder()
                .title("TripAI API 문서")
                .version("0.0.1")
                .description("2023 캡스톤디자인종합프로젝트Ⅰ \n" +
                        "TripAI API 문서입니다!")
                .build();
    }

    private Predicate<RequestHandler> getApis() {
        // Controller 생성 시에 이곳에 경로 추가
        return RequestHandlerSelectors.basePackage("com.milkcow.tripai.article.controller")
                .or(RequestHandlerSelectors.basePackage("com.milkcow.tripai.member.controller"))
                .or(RequestHandlerSelectors.basePackage("com.milkcow.tripai.plan.controller"))
                .or(RequestHandlerSelectors.basePackage("com.milkcow.tripai.image.controller"));
    }

    @Bean
    public Docket swaggerApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .ignoredParameterTypes(AuthenticationPrincipal.class)
                .securityContexts(Arrays.asList(securityContext()))
                .securitySchemes(Arrays.asList(apiKey()))
                .consumes(getConsumeContentTypes())
                .produces(getProduceContentTypes())
                .apiInfo(swaggerInfo()).select()
                .apis(getApis())
                .paths(PathSelectors.any())
                .build()
                .useDefaultResponseMessages(false);
    }

    // 인증 방식 설정
    private SecurityContext securityContext(){
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .build();
    }

    private List<SecurityReference> defaultAuth(){
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return List.of(new SecurityReference("Authorization", authorizationScopes));
    }

    // 버튼 클릭 시 입력 값 설정
    private ApiKey apiKey(){
        return new ApiKey("Authorization", "Authorization", "header");
    }

    private Set<String> getConsumeContentTypes() {
        Set<String> consumes = new HashSet<>();
        consumes.add("application/json;charset=UTF-8");
        consumes.add("application/x-www-form-urlencoded");
        return consumes;
    }

    private Set<String> getProduceContentTypes() {
        Set<String> produces = new HashSet<>();
        produces.add("application/json;charset=UTF-8");
        return produces;
    }

}
