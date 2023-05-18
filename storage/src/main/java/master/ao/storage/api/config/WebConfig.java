package master.ao.storage.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;


@Configuration
@EnableSwagger2
public class WebConfig  {


    private final ResponseMessage m200 = simpleMessage(200,"Chamada realizada com sucesso!");
    private final ResponseMessage m201 = simpleMessage(201,"Recurso criado!");
    private final ResponseMessage m204 = simpleMessage(204,"Actualização feita com sucesso!");
    private final ResponseMessage m401 = simpleMessage(401,"Autorização é requerida");
    private final ResponseMessage m403 = simpleMessage(403,"Sem autorização para acessar este recurso!");
    private final ResponseMessage m404 = simpleMessage(404,"Objecto não encontrado");
    private final ResponseMessage m422 = simpleMessage(422,"Erro de validação");
    private final ResponseMessage m500 = simpleMessage(500,"Erro interno do servidor.");


    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .useDefaultResponseMessages(false)
                .globalResponseMessage(RequestMethod.POST, Arrays.asList(m200,m201,m403,m422,m500))
                .globalResponseMessage(RequestMethod.PUT, Arrays.asList(m204,m403,m422,m500))
                .globalResponseMessage(RequestMethod.GET, Arrays.asList(m403,m404,m500))
                .globalResponseMessage(RequestMethod.DELETE, Arrays.asList(m204,m403,m404))
                .select()
                .apis(RequestHandlerSelectors.basePackage("master.ao.storage.api.controllers"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("")
                .version("v1.0.0")
                .description("Sistema de Gestão Hospitalar - Modulo Storage")
                .build();
    }


    private ResponseMessage simpleMessage(int code, String msg) {
        return new ResponseMessageBuilder().code(code).message(msg).build();
    }

}