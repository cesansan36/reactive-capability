package com.rutaaprendizajewebflux.capability.configuration.beanconfiguration;

import com.rutaaprendizajewebflux.capability.application.handler.IBootcampCapabilityHandler;
import com.rutaaprendizajewebflux.capability.application.handler.ICapabilityHandler;
import com.rutaaprendizajewebflux.capability.application.handler.ISoloCapabilityHandler;
import com.rutaaprendizajewebflux.capability.application.handler.impl.BootcampCapabilityHandler;
import com.rutaaprendizajewebflux.capability.application.handler.impl.CapabilityHandler;
import com.rutaaprendizajewebflux.capability.application.handler.impl.SoloCapabilityHandler;
import com.rutaaprendizajewebflux.capability.application.mapper.IBootcampCapabilityRequestMapper;
import com.rutaaprendizajewebflux.capability.application.mapper.IBootcampCapabilityResponseMapper;
import com.rutaaprendizajewebflux.capability.application.mapper.ICapabilityPlusTechnologiesRequestMapper;
import com.rutaaprendizajewebflux.capability.application.mapper.ICapabilityPlusTechnologiesResponseMapper;
import com.rutaaprendizajewebflux.capability.application.mapper.ISoloCapabilityResponseMapper;
import com.rutaaprendizajewebflux.capability.application.mapper.impl.BootcampCapabilityRequestMapper;
import com.rutaaprendizajewebflux.capability.application.mapper.impl.BootcampCapabilityResponseMapper;
import com.rutaaprendizajewebflux.capability.application.mapper.impl.CapabilityPlusTechnologiesRequestMapper;
import com.rutaaprendizajewebflux.capability.application.mapper.impl.CapabilityPlusTechnologiesResponseMapper;
import com.rutaaprendizajewebflux.capability.application.mapper.impl.SoloCapabilityResponseMapper;
import com.rutaaprendizajewebflux.capability.domain.ports.in.IBootcampCapabilityServicePort;
import com.rutaaprendizajewebflux.capability.domain.ports.in.ICapabilityServicePort;
import com.rutaaprendizajewebflux.capability.domain.ports.in.IReadCapabilityServicePort;
import com.rutaaprendizajewebflux.capability.domain.ports.in.ISaveCapabilityServicePort;
import com.rutaaprendizajewebflux.capability.domain.ports.out.IBootcampCapabilityPersistencePort;
import com.rutaaprendizajewebflux.capability.domain.ports.out.ICapabilityPersistencePort;
import com.rutaaprendizajewebflux.capability.domain.ports.out.ITechnologyCommunicationPort;
import com.rutaaprendizajewebflux.capability.domain.usecase.BootcampCapabilityUseCase;
import com.rutaaprendizajewebflux.capability.domain.usecase.ReadCapabilityUseCase;
import com.rutaaprendizajewebflux.capability.domain.usecase.SaveCapabilityUseCase;
import com.rutaaprendizajewebflux.capability.domain.usecase.SoloCapabilityUseCase;
import com.rutaaprendizajewebflux.capability.infrastructure.secondary.adapter.BootcampCapabilityRelationPersistenceAdapter;
import com.rutaaprendizajewebflux.capability.infrastructure.secondary.adapter.CapabilityPersistenceAdapter;
import com.rutaaprendizajewebflux.capability.infrastructure.secondary.adapter.TechnologyWebClientAdapter;
import com.rutaaprendizajewebflux.capability.infrastructure.secondary.mapper.IBootcampCapabilityEntityMapper;
import com.rutaaprendizajewebflux.capability.infrastructure.secondary.mapper.ICapabilityPlusTechnologyEntityMapper;
import com.rutaaprendizajewebflux.capability.infrastructure.secondary.mapper.ICapabilityPlusTechnologyWebclientMapper;
import com.rutaaprendizajewebflux.capability.infrastructure.secondary.mapper.ISoloCapabilityEntityMapper;
import com.rutaaprendizajewebflux.capability.infrastructure.secondary.mapper.impl.BootcampCapabilityEntityMapper;
import com.rutaaprendizajewebflux.capability.infrastructure.secondary.mapper.impl.CapabilityPlusTechnologyEntityMapper;
import com.rutaaprendizajewebflux.capability.infrastructure.secondary.mapper.impl.CapabilityPlusTechnologyWebclientMapper;
import com.rutaaprendizajewebflux.capability.infrastructure.secondary.mapper.impl.SoloCapabilityEntityMapper;
import com.rutaaprendizajewebflux.capability.infrastructure.secondary.repository.IBootcampCapabilityRepository;
import com.rutaaprendizajewebflux.capability.infrastructure.secondary.repository.ICapabilityRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.transaction.reactive.TransactionalOperator;
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
                                                                ICapabilityPlusTechnologyEntityMapper capabilityPlusTechnologyEntityMapper,
                                                                R2dbcEntityTemplate r2dbcEntityTemplate) {
        return new CapabilityPersistenceAdapter(capabilityRepository, soloCapabilityEntityMapper, capabilityPlusTechnologyEntityMapper, r2dbcEntityTemplate);
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

    @Bean
    public ISaveCapabilityServicePort saveCapabilityServicePort(
            ICapabilityPersistencePort capabilityPersistencePort,
            ITechnologyCommunicationPort technologyCommunicationPort,
            TransactionalOperator transactionalOperator) {

        return new SaveCapabilityUseCase(capabilityPersistencePort, technologyCommunicationPort, transactionalOperator);
    }

    @Bean
    public ICapabilityPlusTechnologiesResponseMapper capabilityPlusTechnologiesResponseMapper() {
        return new CapabilityPlusTechnologiesResponseMapper();
    }

    @Bean
    public ICapabilityPlusTechnologiesRequestMapper capabilityPlusTechnologiesRequestMapper() {
        return new CapabilityPlusTechnologiesRequestMapper();
    }

    @Bean
    public IReadCapabilityServicePort readCapabilityServicePort(
            ITechnologyCommunicationPort technologyCommunicationPort,
            ICapabilityPersistencePort capabilityPersistencePort
    ) {
        return new ReadCapabilityUseCase(technologyCommunicationPort, capabilityPersistencePort);
    }

    @Bean
    public ICapabilityHandler capabilityHandler(
            ISaveCapabilityServicePort saveCapabilityServicePort,
            IReadCapabilityServicePort readCapabilityServicePort,
            ICapabilityPlusTechnologiesResponseMapper capabilityPlusTechnologiesResponseMapper,
            ICapabilityPlusTechnologiesRequestMapper capabilityPlusTechnologiesRequestMapper
    ) {
        return new CapabilityHandler(saveCapabilityServicePort, readCapabilityServicePort, capabilityPlusTechnologiesResponseMapper, capabilityPlusTechnologiesRequestMapper);
    }

    @Bean
    public IBootcampCapabilityHandler bootcampCapabilityHandler(
            IBootcampCapabilityServicePort bootcampCapabilityServicePort,
            IBootcampCapabilityRequestMapper bootcampCapabilityRequestMapper,
            IBootcampCapabilityResponseMapper bootcampCapabilityResponseMapper
    ) {
        return new BootcampCapabilityHandler(bootcampCapabilityServicePort, bootcampCapabilityRequestMapper, bootcampCapabilityResponseMapper);
    }

    @Bean
    public IBootcampCapabilityRequestMapper bootcampCapabilityRequestMapper() {
        return new BootcampCapabilityRequestMapper();
    }

    @Bean
    public IBootcampCapabilityResponseMapper bootcampCapabilityResponseMapper() {
        return new BootcampCapabilityResponseMapper();
    }

    @Bean
    public IBootcampCapabilityServicePort bootcampCapabilityServicePort(
            IReadCapabilityServicePort readCapabilityServicePort,
            IBootcampCapabilityPersistencePort bootcampCapabilityPersistencePort
    ) {
        return new BootcampCapabilityUseCase(readCapabilityServicePort, bootcampCapabilityPersistencePort);
    }

    @Bean
    public IBootcampCapabilityPersistencePort bootcampCapabilityPersistencePort(
            IBootcampCapabilityRepository bootcampCapabilityRepository,
            IBootcampCapabilityEntityMapper bootcampCapabilityEntityMapper,
            R2dbcEntityTemplate r2dbcEntityTemplate
    ) {
        return new BootcampCapabilityRelationPersistenceAdapter(bootcampCapabilityRepository, bootcampCapabilityEntityMapper, r2dbcEntityTemplate);
    }

    @Bean
    public IBootcampCapabilityEntityMapper bootcampCapabilityEntityMapper() {
        return new BootcampCapabilityEntityMapper();
    }
}
