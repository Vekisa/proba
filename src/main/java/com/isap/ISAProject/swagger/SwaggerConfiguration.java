package com.isap.ISAProject.swagger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StopWatch;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.isap.ISAProject.controller.airline.AirlineController;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration

@EnableSwagger2
@ComponentScan(basePackageClasses = {
        AirlineController.class,			// pretpostavljam da se ovde navode ostali rest kontroleri kada se napravi dokumentacija njihova, ostatak klase pojma nemam sta radi
})
public class SwaggerConfiguration {
	
	 private final Logger log = LoggerFactory.getLogger(SwaggerConfiguration.class);

	    @Bean
	    public Docket swaggerSpringfoxDocket() {

	        log.debug("Initializing swagger...");

	        final StopWatch watch = new StopWatch();
	        watch.start();

	        /*
	         * select() metoda vraca instancu ApiSelectorBuilder koja
	         * pruza nacin za kontrolu endpointa koje otkriva Swagger
	         */
	        final Docket docket = new Docket(DocumentationType.SWAGGER_2)
	                .apiInfo(apiInfo())
	                .select()
	                .paths(apiPaths())
	                .build();

	        watch.stop();
	        log.debug("Swagger started in {} ms", watch.getTotalTimeMillis());
	        return docket;
	    }
	    
	    @SuppressWarnings("unchecked")
		private Predicate<String> apiPaths() {
	        return Predicates.or(PathSelectors.regex("/.*"));
	    }

	    private ApiInfo apiInfo() {
	        return new ApiInfoBuilder()
	                .title("Swagger 2 API documentation example")
	                .description("The reference documentation for ISA project example.")
	                .termsOfServiceUrl("http://enastava.ftninformatika.com")
	                .build();
	    }

}
