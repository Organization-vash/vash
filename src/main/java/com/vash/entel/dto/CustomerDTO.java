package com.vash.entel.dto;
import com.vash.entel.model.enums.DocumentType;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class CustomerDTO {

    private Integer id;
    @Setter
    @Getter
    @NotNull(message = "El documento es obligatorio")
    @Min(value = 100000, message = "El número de documento debe tener al menos 6 dígitos")
    @Max(value = 999999999999999999L, message = "El número de documento debe tener como máximo 20 dígitos")
    private Long docNumber;

    @Setter
    @Getter
    @Pattern(regexp = "^[A-Z ]+$", message = "El nombre solo puede contener letras mayúsculas")
    @NotBlank(message = "El nombre no puede estar vacío")
    private String fullname;

    @NotNull(message = "El tipo de documento es obligatorio")
    private DocumentType documentType;

    public CustomerDTO() {}

    public CustomerDTO(Integer id, Long docNumber, String fullname, DocumentType documentType) {
        this.id = id;
        this.docNumber = docNumber;
        this.fullname = fullname;
        this.documentType = documentType;
    }
}
