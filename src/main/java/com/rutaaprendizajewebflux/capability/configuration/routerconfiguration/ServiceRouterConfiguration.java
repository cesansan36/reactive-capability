package com.rutaaprendizajewebflux.capability.configuration.routerconfiguration;

import com.rutaaprendizajewebflux.capability.application.handler.IBootcampCapabilityHandler;
import com.rutaaprendizajewebflux.capability.application.handler.ICapabilityHandler;
import com.rutaaprendizajewebflux.capability.application.handler.ISoloCapabilityHandler;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static com.rutaaprendizajewebflux.capability.util.Constants.CAPABILITIES_PATH;
import static com.rutaaprendizajewebflux.capability.util.Constants.GET_BY_BOOTCAMP_ID_SUB_PATH;
import static com.rutaaprendizajewebflux.capability.util.Constants.GET_BY_ID_SUB_PATH;
import static com.rutaaprendizajewebflux.capability.util.Constants.LINKED_BOOTCAMP_CAPABILITIES_PATH;

@Configuration
public class ServiceRouterConfiguration {

    @Bean
    public WebProperties.Resources resources() {
        return new WebProperties.Resources();
    }

    @Bean
    public RouterFunction<ServerResponse> router(ISoloCapabilityHandler soloCapabilityHandler,
                                                 ICapabilityHandler capabilityHandler,
                                                 IBootcampCapabilityHandler bootcampCapabilityHandler) {
        return RouterFunctions.route()
                .GET(CAPABILITIES_PATH + GET_BY_ID_SUB_PATH, soloCapabilityHandler::findById)
                .GET(CAPABILITIES_PATH, capabilityHandler::findAllPaginated)
                .POST(CAPABILITIES_PATH, capabilityHandler::save)
                .GET(LINKED_BOOTCAMP_CAPABILITIES_PATH + GET_BY_BOOTCAMP_ID_SUB_PATH, bootcampCapabilityHandler::findCapabilitiesByBootcampId)
                .GET(LINKED_BOOTCAMP_CAPABILITIES_PATH, bootcampCapabilityHandler::findPaginatedBootcampByCapabilityAmount)
                .POST(LINKED_BOOTCAMP_CAPABILITIES_PATH, bootcampCapabilityHandler::save)
                .build();
    }
}
