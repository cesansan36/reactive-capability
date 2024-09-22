package com.rutaaprendizajewebflux.capability.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LinkBootcampWithCapabilitiesRequest {

    Long bootcampId;
    List<String> capabilitiesNames;

}
