package com.rutaaprendizajewebflux.capability.configuration.routerconfiguration;

import com.rutaaprendizajewebflux.capability.application.handler.ISoloCapabilityHandler;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class ServiceRouterConfiguration {

    @Bean
    public WebProperties.Resources resources() {
        return new WebProperties.Resources();
    }

    @Bean
    public RouterFunction<ServerResponse> router(ISoloCapabilityHandler soloCapabilityHandler) {
        return RouterFunctions.route()
                .GET("/capabilities/{id}", soloCapabilityHandler::findById)
                .build();
    }
}
