package com.vash.entel.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ServiceDTO {
    private int id;

    @NotBlank(message = "El nombre del servicio es obligatorio")
    @Size(max = 25, message = "El nombre del servicio debe tener 25 caracteres o menos")
    private String name;

    @NotBlank(message = "El tipo del servicio es obligatorio")
    @Size(max = 9, message = "El tipo del servicio debe tener 9 caracteres o menos")
    private String type;

    @NotBlank(message = "La descripción del servicio es obligatorio")
    @Size(max = 500, message = "La descripción del servicio debe tener 500 caracteres o menos")
    private String description;
}
