package com.vash.entel.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        // Crear e inicializar un nuevo ModelMapper
        ModelMapper modelMapper = new ModelMapper();

        // Puedes agregar configuraciones específicas aquí si lo necesitas

        return modelMapper;
    }
}
