package com.vash.entel.dto;

import com.vash.entel.model.enums.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UserDTO {
    private Integer id;

    @NotBlank(message = "El documento es obligatorio")
    private String document;

    @Min(value = 10000000, message = "El número de documento debe tener al menos 8 dígitos")
    @Max(value = 999999999, message = "El número de documento debe tener como mínimo 8 y máximo 9 dígitos")
    @NotNull(message = "El número de documento es obligatorio")
    private Integer numberDoc;

    @NotBlank(message = "El nombre es obligatorio")
    @Pattern(regexp = "^[a-zA-Z ]+$", message = "El nombre solo puede contener letras")
    private String name;

    @NotBlank(message = "El apellido es obligatorio")
    @Pattern(regexp = "^[a-zA-Z ]+$", message = "El apellido solo puede contener letras")
    private String lastName;

    @NotBlank(message = "El email es obligatorio")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, max = 12, message = "La contraseña debe tener entre 8 a 12 caracteres")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!#%*?&])[A-Za-z\\d@$!#%*?&]{8,12}$",
            message = "La contraseña debe tener al menos una mayúscula, un número y un carácter especial"
    )
    private String password;

    @NotNull(message = "El rol es obligatorio")
    private Role role;

    public UserDTO() {
    }

    //Constructor, Getters y Setters
    public UserDTO(Integer id, String document, Integer numberDoc, String name, String lastName, String email, String password, Role role) {
        this.id = id;
        this.document = document;
        this.numberDoc = numberDoc;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = role;
    }
}