package com.vash.entel.mapper;

import com.vash.entel.dto.UserDTO;
import com.vash.entel.model.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private final ModelMapper modelMapper;
    public UserMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    // Convertir de User a UserDTO
    public UserDTO toUserDTO (User user){
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        return userDTO;
    }

    // Convertir de UserDTO a User
    public User toUserEntity (UserDTO userDTO){
        return modelMapper.map(userDTO, User.class);
    }
}
