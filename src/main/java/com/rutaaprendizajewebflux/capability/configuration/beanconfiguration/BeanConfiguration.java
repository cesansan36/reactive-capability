package com.rutaaprendizajewebflux.capability.configuration.beanconfiguration;

import com.rutaaprendizajewebflux.capability.application.handler.ISoloCapabilityHandler;
import com.rutaaprendizajewebflux.capability.application.handler.impl.SoloCapabilityHandler;
import com.rutaaprendizajewebflux.capability.application.mapper.ISoloCapabilityResponseMapper;
import com.rutaaprendizajewebflux.capability.application.mapper.impl.SoloCapabilityResponseMapper;
import com.rutaaprendizajewebflux.capability.domain.ports.in.ICapabilityServicePort;
import com.rutaaprendizajewebflux.capability.domain.ports.out.ICapabilityPersistencePort;
import com.rutaaprendizajewebflux.capability.domain.ports.out.ITechnologyCommunicationPort;
import com.rutaaprendizajewebflux.capability.domain.usecase.SoloCapabilityUseCase;
import com.rutaaprendizajewebflux.capability.infrastructure.secondary.adapter.CapabilityPersistenceAdapter;
import com.rutaaprendizajewebflux.capability.infrastructure.secondary.adapter.TechnologyWebClientAdapter;
import com.rutaaprendizajewebflux.capability.infrastructure.secondary.mapper.ICapabilityPlusTechnologyEntityMapper;
import com.rutaaprendizajewebflux.capability.infrastructure.secondary.mapper.ICapabilityPlusTechnologyWebclientMapper;
import com.rutaaprendizajewebflux.capability.infrastructure.secondary.mapper.ISoloCapabilityEntityMapper;
import com.rutaaprendizajewebflux.capability.infrastructure.secondary.mapper.impl.CapabilityPlusTechnologyEntityMapper;
import com.rutaaprendizajewebflux.capability.infrastructure.secondary.mapper.impl.CapabilityPlusTechnologyWebclientMapper;
import com.rutaaprendizajewebflux.capability.infrastructure.secondary.mapper.impl.SoloCapabilityEntityMapper;
import com.rutaaprendizajewebflux.capability.infrastructure.secondary.repository.ICapabilityRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class BeanConfiguration {

    @Bean
    public ISoloCapabilityEntityMapper soloCapabilityEntityMapper() {
        return new SoloCapabilityEntityMapper();
    }

    @Bean
    public ISoloCapabilityResponseMapper soloCapabilityResponseMapper() {
        return new SoloCapabilityResponseMapper();
    }

    @Bean
    public ICapabilityPlusTechnologyEntityMapper capabilityPlusTechnologyEntityMapper() {
        return new CapabilityPlusTechnologyEntityMapper();
    }

    @Bean
    public ICapabilityPlusTechnologyWebclientMapper capabilityPlusTechnologyWebclientMapper() {
        return new CapabilityPlusTechnologyWebclientMapper();
    }

    @Bean
    public ICapabilityPersistencePort capabilityPersistencePort(ICapabilityRepository capabilityRepository,
                                                                 ISoloCapabilityEntityMapper soloCapabilityEntityMapper,
                                                                ICapabilityPlusTechnologyEntityMapper capabilityPlusTechnologyEntityMapper) {
        return new CapabilityPersistenceAdapter(capabilityRepository, soloCapabilityEntityMapper, capabilityPlusTechnologyEntityMapper);
    }

    @Bean
    public ICapabilityServicePort capabilityServicePort(ICapabilityPersistencePort capabilityPersistencePort) {
        return new SoloCapabilityUseCase(capabilityPersistencePort);
    }

    @Bean
    public ISoloCapabilityHandler soloCapabilityHandler(
            ICapabilityServicePort capabilityServicePort,
            ISoloCapabilityResponseMapper soloCapabilityResponseMapper) {

        return new SoloCapabilityHandler(capabilityServicePort, soloCapabilityResponseMapper);
    }

    @Bean
    public ITechnologyCommunicationPort technologyCommunicationPort(WebClient webClient,
            ICapabilityPlusTechnologyWebclientMapper capabilityPlusTechnologyWebclientMapper) {
        return new TechnologyWebClientAdapter(webClient, capabilityPlusTechnologyWebclientMapper);
    }
}
