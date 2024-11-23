package com.vash.entel.mapper;

import com.vash.entel.dto.AuthResponseDTO;
import com.vash.entel.dto.LoginDTO;
import com.vash.entel.model.entity.Adviser;
import com.vash.entel.model.entity.Module;
import com.vash.entel.dto.UserDTO;
import com.vash.entel.model.entity.Supervisor;
import com.vash.entel.model.entity.User;
import com.vash.entel.model.enums.ERole;
import com.vash.entel.repository.ModuleRepository;
import com.vash.entel.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
public class UserMapper {

    private final ModelMapper modelMapper;
    private final RoleRepository roleRepository;
    private final ModuleRepository moduleRepository;

    // Convertir de entidad a DTO
    public UserDTO toUserDTO(User user) {
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        if (user.getRole() != null) {
            userDTO.setRole(user.getRole().getName()); // Mapea el nombre del rol
        }
        if (user.getAdviser() != null) {
            userDTO.setNumberDoc(user.getAdviser().getNumberDoc());
        } else if (user.getSupervisor() != null) {
            userDTO.setNumberDoc(user.getSupervisor().getNumberDoc());
        }
        return userDTO;
    }

    public User toUserEntity(UserDTO userDTO) {
        User user = modelMapper.map(userDTO, User.class);

        if (userDTO.getModuleId() != null) {
            Module module = moduleRepository.findById(userDTO.getModuleId())
                    .orElseThrow(() -> new RuntimeException("Módulo no encontrado"));

            // Verificar si es un Adviser o Supervisor
            if (userDTO.getRole() == ERole.ADVISER) {
                Adviser adviser = new Adviser();
                adviser.setModule(module);
                adviser.setUser(user);
                adviser.setName(userDTO.getName());
                adviser.setLastName(userDTO.getLastName());
                adviser.setNumberDoc(userDTO.getNumberDoc());
                adviser.setDocumentType(userDTO.getDocumentType());
                adviser.setCreatedAt(LocalDateTime.now());
                adviser.setEmail(userDTO.getEmail());
                adviser.setPassword(userDTO.getPassword());
                adviser.setRole(userDTO.getRole());

                user.setAdviser(adviser);

            } else if (userDTO.getRole() == ERole.SUPERVISOR) {
                Supervisor supervisor = new Supervisor();
                supervisor.setModule(module);
                supervisor.setUser(user);
                supervisor.setName(userDTO.getName());
                supervisor.setLastName(userDTO.getLastName());
                supervisor.setNumberDoc(userDTO.getNumberDoc());
                supervisor.setDocumentType(userDTO.getDocumentType());
                supervisor.setCreatedAt(LocalDateTime.now());
                supervisor.setEmail(userDTO.getEmail());
                supervisor.setPassword(userDTO.getPassword());
                supervisor.setRole(userDTO.getRole());

                user.setSupervisor(supervisor);
            } else {
                throw new IllegalArgumentException("Rol desconocido: " + userDTO.getRole());
            }
        }
        return user;
    }

    // Convertir LoginDTO a User Entity
    public User toUserEntity(LoginDTO loginDTO) {
        return modelMapper.map(loginDTO, User.class);
    }

    // Convertir User y Token a AuthResponseDTO
    public AuthResponseDTO toAuthResponseDTO(User user, String token) {
        AuthResponseDTO authResponseDTO = new AuthResponseDTO();
        authResponseDTO.setToken(token);

        // Obtener el nombre completo (nombre + apellido) basado en si es Adviser, Supervisor o Admin
        String fullName = (user.getAdviser() != null)
                ? user.getAdviser().getName() + " " + user.getAdviser().getLastName()
                : (user.getSupervisor() != null)
                ? user.getSupervisor().getName() + " " + user.getSupervisor().getLastName()
                : "Admin";

        authResponseDTO.setId(user.getId());
        authResponseDTO.setName(fullName);

        // Obtener el rol del usuario
        authResponseDTO.setRole(user.getRole().getName().name());

        return authResponseDTO;
    }
}
