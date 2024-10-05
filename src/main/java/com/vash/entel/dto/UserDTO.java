package com.vash.entel.dto;

import com.vash.entel.model.enums.Role;
import com.vash.entel.model.enums.DocumentType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class UserDTO {
    private Integer id;

    @NotNull(message = "El documento es obligatorio")
    private DocumentType documentType;

    @Min(value = 100000, message = "El número de documento debe tener al menos 6 dígitos")
    @NotNull(message = "El número de documento es obligatorio")
    private Integer numberDoc;

    @NotBlank(message = "El nombre es obligatorio")
    @Pattern(regexp = "^[a-zA-Z ]+$", message = "El nombre solo puede contener letras")
    private String name;

    @NotBlank(message = "El apellido es obligatorio")
    @Pattern(regexp = "^[a-zA-Z ]+$", message = "El apellido solo puede contener letras")
    private String lastName;

    @Setter
    @Getter
    @NotNull(message = "El ID del módulo es obligatorio")
    private Integer moduleId;

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

    private String username;

    public UserDTO(){
    }


    public UserDTO(Integer id, DocumentType documentType, Integer numberDoc, String name, String lastName, Integer moduleId, String email, String password, Role role, String username) {
        this.id = id;
        this.documentType = documentType;
        this.numberDoc = numberDoc;
        this.name = name;
        this.lastName = lastName;
        this.moduleId = moduleId;
        this.email = email;
        this.password = password;
        this.role = role;
        this.username = username;
    }

}
