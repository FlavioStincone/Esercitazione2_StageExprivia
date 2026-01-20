package exprivia.it.documenti.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

    //personalizzazione della pagina di Swagger
    @Bean
    public OpenAPI myOpenAPI() {
        Contact contact = new Contact();
        contact.setEmail("flaviostincone711@gmail.com");
        contact.setName("Exprivia Developer");
        
        Info info = new Info()
                .title("Documenti API Management")
                .version("2.3.0")
                .contact(contact)
                .description("API per la gestione di documenti Pubblici, Confidenziali e Segreti.");

        return new OpenAPI().info(info);
    }

    //raggruppamento degli endopoint per gruppo pubblico/confidenziale/segreto
    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("1-public")
                .pathsToMatch("/document/public/**")
                .build();
    }

    @Bean
    public GroupedOpenApi confidentialApi() {
        return GroupedOpenApi.builder()
                .group("2-confidential")
                .pathsToMatch("/document/confidential/**")
                .build();
    }

    @Bean
    public GroupedOpenApi secretApi() {
        return GroupedOpenApi.builder()
                .group("3-secret")
                .pathsToMatch("/document/secret/**")
                .build();
    }
}