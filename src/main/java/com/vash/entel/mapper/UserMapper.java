package com.vash.entel.mapper;
import com.vash.entel.model.entity.Module;
import com.vash.entel.dto.UserDTO;
import com.vash.entel.model.entity.User;
import com.vash.entel.repository.ModuleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;


@Component
public class UserMapper {

    private final ModelMapper modelMapper;
    private final ModuleRepository moduleRepository;

    public UserMapper(ModelMapper modelMapper, ModuleRepository moduleRepository) {
        this.modelMapper = modelMapper;
        this.moduleRepository = moduleRepository;
    }

    public UserDTO toUserDTO(User user){
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        userDTO.setModuleId(user.getModule().getId()); // Asignar el ID del módulo
        return userDTO;
    }

    public User toUserEntity(UserDTO userDTO) {
        User user = modelMapper.map(userDTO, User.class);
        Module module = moduleRepository.findById(userDTO.getModuleId())
                .orElseThrow(() -> new RuntimeException("Módulo no encontrado"));
        user.setModule(module);  // Asignar el módulo
        return user;
    }
}
