package coffee.store.swagger

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.service.ApiKey
import springfox.documentation.service.Contact
import springfox.documentation.service.SecurityReference
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spi.service.contexts.OperationContext
import springfox.documentation.spi.service.contexts.SecurityContext
import springfox.documentation.spring.web.plugins.Docket
import java.util.*


@Configuration
class SpringFoxConfig : WebMvcConfigurationSupport() {
    @Bean
    fun api(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .securityContexts(listOf(securityContext()))
                .securitySchemes(listOf(apiKey()))
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
    }

    private fun apiKey(): ApiKey {
        return ApiKey("JWT", "Authorization", "header")
    }

    private fun apiInfo() = ApiInfo(
            "Coffee Store",
            "Coffee store description of API.",
            "0.2.0",
            "Terms of service",
            Contact("Georgii Savin", "", ""),
            "License of API",
            "API license URL",
            Collections.emptyList())

    private fun securityContext(): SecurityContext {
        return SecurityContext.builder()
                .securityReferences(listOf(defaultAuth()))
                .operationSelector { o: OperationContext -> o.requestMappingPattern().matches(Regex("/.*")) }
                .build()
    }

    private fun defaultAuth(): SecurityReference? {
        return SecurityReference.builder()
                .scopes(arrayOfNulls(0))
                .reference("JWT")
                .build()
    }

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/")
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/")
    }
}