package com.vash.entel.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class CustomerDTO {
    @Setter
    @Getter
    @NotNull(message = "El documento es obligatorio")
    @Min(value = 100000, message = "El número de documento debe tener al menos 6 dígitos")
    @Max(value = 999999999999999999L, message = "El número de documento debe tener como máximo 20 dígitos")
    private Long docNumber;

    @Setter
    @Getter
    @Pattern(regexp = "^[A-Z ]+$", message = "El nombre solo puede contener letras mayúsculas")
    private String fullname;


    public CustomerDTO() {}

    public CustomerDTO(Long docNumber, String fullname) {
        this.docNumber = docNumber;
        this.fullname = fullname;
    }
}
