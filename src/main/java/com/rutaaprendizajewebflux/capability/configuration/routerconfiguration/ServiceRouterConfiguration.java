package com.rutaaprendizajewebflux.capability.configuration.routerconfiguration;

import com.rutaaprendizajewebflux.capability.application.dto.request.LinkBootcampWithCapabilitiesRequest;
import com.rutaaprendizajewebflux.capability.application.dto.request.SaveCapabilityPlusTechnologiesRequest;
import com.rutaaprendizajewebflux.capability.application.dto.response.BootcampWithCapabilitiesResponse;
import com.rutaaprendizajewebflux.capability.application.dto.response.CapabilityPlusTechnologiesResponse;
import com.rutaaprendizajewebflux.capability.application.dto.response.SoloCapabilityResponse;
import com.rutaaprendizajewebflux.capability.application.handler.IBootcampCapabilityHandler;
import com.rutaaprendizajewebflux.capability.application.handler.ICapabilityHandler;
import com.rutaaprendizajewebflux.capability.application.handler.ISoloCapabilityHandler;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import org.springdoc.core.fn.builders.operation.Builder;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.util.function.Consumer;

import static com.rutaaprendizajewebflux.capability.util.Constants.CAPABILITIES_PATH;
import static com.rutaaprendizajewebflux.capability.util.Constants.GET_BY_BOOTCAMP_ID_SUB_PATH;
import static com.rutaaprendizajewebflux.capability.util.Constants.GET_BY_ID_SUB_PATH;
import static com.rutaaprendizajewebflux.capability.util.Constants.LINKED_BOOTCAMP_CAPABILITIES_PATH;
import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;
import static org.springdoc.core.fn.builders.parameter.Builder.parameterBuilder;
import static org.springdoc.core.fn.builders.requestbody.Builder.requestBodyBuilder;
import static org.springdoc.webflux.core.fn.SpringdocRouteBuilder.route;

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
        return route()
                /*💤*/.GET(CAPABILITIES_PATH + GET_BY_ID_SUB_PATH, soloCapabilityHandler::findById, getSoloCapabilityConsumer())
                /*⭐*/.POST(CAPABILITIES_PATH, capabilityHandler::save, saveConsumer())
                /*⭐*/.GET(CAPABILITIES_PATH, capabilityHandler::findAllPaginated, findAllPaginatedConsumer())
                /*🥈*/.POST(LINKED_BOOTCAMP_CAPABILITIES_PATH, bootcampCapabilityHandler::save, saveLinkedBootcampCapabilityConsumer())
                /*🥈*/.GET(LINKED_BOOTCAMP_CAPABILITIES_PATH + GET_BY_BOOTCAMP_ID_SUB_PATH, bootcampCapabilityHandler::findCapabilitiesByBootcampId, findCapabilitiesByBootcampIdConsumer())
                /*🥈*/.GET(LINKED_BOOTCAMP_CAPABILITIES_PATH, bootcampCapabilityHandler::findPaginatedBootcampByCapabilityAmount, findPaginatedBootcampByCapabilityAmountConsumer())
                .build();
    }

    private Consumer<Builder> getSoloCapabilityConsumer() {

        return builder -> builder
                .operationId("getSoloCapabilityById")
                .summary("💤 Get solo capability by id")
                .tag("💤 Experimentación")
                .parameter(parameterBuilder().name("id").description("Id of the solo capability").in(ParameterIn.PATH).required(true))
                .response(responseBuilder().responseCode("200").description("Capability with no technologies").implementation(SoloCapabilityResponse.class));
    }

    private Consumer<Builder> saveConsumer() {

        return builder -> builder
                .operationId("saveCapability")
                .summary("⭐ Save Capability")
                .tag("⭐ Solo Capacidades")
                .requestBody(requestBodyBuilder().implementation(SaveCapabilityPlusTechnologiesRequest.class))
                .response(responseBuilder().responseCode("200").description("OK").implementation(CapabilityPlusTechnologiesResponse.class));
    }

    private Consumer<Builder> findAllPaginatedConsumer() {

        return builder -> builder
                .operationId("findAllPaginated")
                .summary("⭐ Find All Paginated")
                .tag("⭐ Solo Capacidades")
                .parameter(parameterBuilder().name("page").description("Page number").in(ParameterIn.QUERY).required(true).example("0"))
                .parameter(parameterBuilder().name("size").description("Page size").in(ParameterIn.QUERY).required(true).example("3"))
                .parameter(parameterBuilder().name("sortBy").description("Sort by").in(ParameterIn.QUERY).required(true).example("name"))
                .parameter(parameterBuilder().name("direction").description("Sort direction").in(ParameterIn.QUERY).required(true).example("ASC"))
                .response(responseBuilder().responseCode("200").description("OK").implementationArray(CapabilityPlusTechnologiesResponse.class));
    }

    private Consumer<Builder> saveLinkedBootcampCapabilityConsumer() {

        return builder -> builder
                .operationId("saveLinkedBootcampCapability")
                .summary("🥈 Save Bootcamp_Capability relation")
                .tag("⭐ Relación entre Bootcamp y Capacidad")
                .requestBody(requestBodyBuilder().implementation(LinkBootcampWithCapabilitiesRequest.class))
                .response(responseBuilder().responseCode("200").description("OK").implementation(BootcampWithCapabilitiesResponse.class));
    }

    private Consumer<Builder> findCapabilitiesByBootcampIdConsumer() {

        return builder -> builder
                .operationId("findCapabilitiesByBootcampId")
                .summary("🥈 Find Capabilities by Bootcamp Id")
                .tag("⭐ Relación entre Bootcamp y Capacidad")
                .parameter(parameterBuilder().name("bootcampId").description("Id of the Bootcamp").in(ParameterIn.PATH).required(true))
                .response(responseBuilder().responseCode("200").description("OK").implementation(BootcampWithCapabilitiesResponse.class));
    }

    private Consumer<Builder> findPaginatedBootcampByCapabilityAmountConsumer() {

        return builder -> builder
                .operationId("findPaginatedBootcampByCapabilityAmount")
                .summary("🥈 Find Paginated Bootcamp by Capability Amount")
                .tag("⭐ Relación entre Bootcamp y Capacidad")
                .parameter(parameterBuilder().name("page").description("Page number").in(ParameterIn.QUERY).required(true).example("0"))
                .parameter(parameterBuilder().name("size").description("Page size").in(ParameterIn.QUERY).required(true).example("3"))
                .parameter(parameterBuilder().name("direction").description("Sort direction").in(ParameterIn.QUERY).required(true).example("ASC"))
                .response(responseBuilder().responseCode("200").description("OK").implementationArray(BootcampWithCapabilitiesResponse.class));
    }
}
