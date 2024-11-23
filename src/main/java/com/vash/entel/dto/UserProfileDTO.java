package com.vash.entel.dto;

import com.vash.entel.model.enums.ERole;
import lombok.Data;

@Data
public class UserProfileDTO {

    private int id;
    private String name;
    private String email;
    private ERole role; // El rol puede ser ADMIN, READER o CREATOR

    private String biography;
}