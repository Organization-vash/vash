package com.vash.entel.service.impl;

import com.vash.entel.dto.AuthResponseDTO;
import com.vash.entel.dto.LoginDTO;
import com.vash.entel.dto.UserDTO;
import com.vash.entel.exception.BadRequestException;
import com.vash.entel.exception.ResourceNotFoundException;
import com.vash.entel.mapper.UserMapper;
import com.vash.entel.model.entity.Adviser;
import com.vash.entel.model.entity.Supervisor;
import com.vash.entel.model.entity.User;
import com.vash.entel.model.enums.DocumentType;
import com.vash.entel.model.enums.ERole;
import com.vash.entel.repository.AdviserRepository;
import com.vash.entel.repository.ModuleRepository;
import com.vash.entel.repository.SupervisorRepository;
import com.vash.entel.repository.UserRepository;
import com.vash.entel.security.TokenProvider;
import com.vash.entel.security.UserPrincipal;
import com.vash.entel.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ModuleRepository moduleRepository;
    private final AdviserRepository adviserRepository;
    private final SupervisorRepository supervisorRepository;
    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;

    @Transactional
    @Override
    public AuthResponseDTO login(LoginDTO loginDTO) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword())
        );
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        User user = userPrincipal.getUser();

        String token = tokenProvider.createAccessToken(authentication);

        AuthResponseDTO responseDTO = userMapper.toAuthResponseDTO(user,token);

        return responseDTO;
    }

    @Transactional
    @Override
    public List<UserDTO> getAll() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(userMapper::toUserDTO)
                .toList();
    }

    @Transactional
    @Override
    public UserDTO createUser(UserDTO userDTO) {
        // Validar restricciones
        userRepository.findByEmail(userDTO.getEmail())
                .ifPresent(existingUser -> {
                    throw new BadRequestException("El email ya existe.");
                });

        // Validar duplicados en advisers y supervisors
        adviserRepository.findByNumberDoc(userDTO.getNumberDoc())
                .ifPresent(existingAdviser -> {
                    throw new BadRequestException("El documento ya existe para un asesor.");
                });

        supervisorRepository.findByNumberDoc(userDTO.getNumberDoc())
                .ifPresent(existingSupervisor -> {
                    throw new BadRequestException("El documento ya existe para un supervisor.");
                });

        validateDocument(userDTO);

        // Crear usuario base y guardarlo primero
        User user = userMapper.toUserEntity(userDTO);
        User savedUser = userRepository.save(user);

        // Relacionar con Adviser o Supervisor si aplica
        if (userDTO.getRole() == ERole.ADVISER) {
            Adviser adviser = new Adviser();
            adviser.setName(userDTO.getName());
            adviser.setLastName(userDTO.getLastName());
            adviser.setNumberDoc(userDTO.getNumberDoc());
            adviser.setCreatedAt(LocalDateTime.now());
            adviser.setModule(moduleRepository.findById(userDTO.getModuleId())
                    .orElseThrow(() -> new BadRequestException("El módulo no existe.")));
            adviser.setUser(savedUser); // Asociar el usuario ya guardado
            adviserRepository.save(adviser);
        } else if (userDTO.getRole() == ERole.SUPERVISOR) {
            Supervisor supervisor = new Supervisor();
            supervisor.setName(userDTO.getName());
            supervisor.setLastName(userDTO.getLastName());
            supervisor.setNumberDoc(userDTO.getNumberDoc());
            supervisor.setCreatedAt(LocalDateTime.now());
            supervisor.setModule(moduleRepository.findById(userDTO.getModuleId())
                    .orElseThrow(() -> new BadRequestException("El módulo no existe.")));
            supervisor.setUser(savedUser); // Asociar el usuario ya guardado
            supervisorRepository.save(supervisor);
        }

        // Retornar el DTO del usuario
        return userMapper.toUserDTO(savedUser);
    }





    @Transactional
    @Override
    public UserDTO findById(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("El usuario con ID " + id + " no fue encontrado"));
        return userMapper.toUserDTO(user);
    }

    @Transactional
    @Override
    public UserDTO updateUser(Integer id, UserDTO userDTO) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("El usuario con ID " + id + " no fue encontrado"));

        // Validaciones de unicidad
        userRepository.findByEmail(userDTO.getEmail())
                .ifPresent(user -> {
                    if (!user.getId().equals(id)) {
                        throw new BadRequestException("El email ya está en uso");
                    }
                });

        userRepository.findByNumberDoc(userDTO.getNumberDoc())
                .ifPresent(user -> {
                    if (!user.getId().equals(id)) {
                        throw new BadRequestException("El documento ya está en uso");
                    }
                });

        validateDocument(userDTO);

        // Actualizar información base
        existingUser.setEmail(userDTO.getEmail());

        if (userDTO.getPassword() != null && !userDTO.getPassword().isBlank()) {
            existingUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }

        // Actualizar Adviser o Supervisor
        if (existingUser.getAdviser() != null) {
            Adviser adviser = existingUser.getAdviser();
            adviser.setName(userDTO.getName());
            adviser.setLastName(userDTO.getLastName());
            adviser.setNumberDoc(userDTO.getNumberDoc());
            adviser.setUpdatedAt(LocalDateTime.now());
            adviser.setModule(moduleRepository.findById(userDTO.getModuleId())
                    .orElseThrow(() -> new BadRequestException("El módulo no existe")));
            adviserRepository.save(adviser);
        } else if (existingUser.getSupervisor() != null) {
            Supervisor supervisor = existingUser.getSupervisor();
            supervisor.setName(userDTO.getName());
            supervisor.setLastName(userDTO.getLastName());
            supervisor.setNumberDoc(userDTO.getNumberDoc());
            supervisor.setUpdatedAt(LocalDateTime.now());
            supervisor.setModule(moduleRepository.findById(userDTO.getModuleId())
                    .orElseThrow(() -> new BadRequestException("El módulo no existe")));
            supervisorRepository.save(supervisor);
        }

        User updatedUser = userRepository.save(existingUser);

        return userMapper.toUserDTO(updatedUser);
    }


    @Transactional
    @Override
    public void delete(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("El usuario con ID " + id + " no fue encontrado"));
        userRepository.delete(user);
    }

    @Transactional
    @Override
    public List<UserDTO> findByNumberDocOrName(Integer numberDoc, String name) {
        List<User> users = userRepository.findByNumberDocOrName(numberDoc, name);
        return users.stream()
                .map(userMapper::toUserDTO)
                .toList();
    }

    public void validateDocument(UserDTO userDTO) {
        int numberLength = String.valueOf(userDTO.getNumberDoc()).length();

        if (userDTO.getDocumentType() == DocumentType.DNI && numberLength != 8) {
            throw new IllegalArgumentException("El DNI debe tener exactamente 8 dígitos.");
        } else if ((userDTO.getDocumentType() == DocumentType.PASAPORTE ||
                userDTO.getDocumentType() == DocumentType.PTP ||
                userDTO.getDocumentType() == DocumentType.CARNET_EXTRANJERIA)
                && (numberLength < 6 || numberLength > 20)) {
            throw new IllegalArgumentException("El número de documento debe tener entre 6 y 20 dígitos para documentos extranjeros.");
        }
    }

    @Override
    public String generateUsername(String firstName, String lastName, Integer moduleId) {
        // Obtén la primera letra del primer nombre en mayúsculas
        String firstInitial = firstName.substring(0, 1).toUpperCase();

        // Convierte el apellido completo a mayúsculas
        String fullLastName = lastName.toUpperCase();

        // Combina los valores con el ID del módulo
        String generatedUsername = firstInitial + fullLastName + moduleId;

        // Verifica si el nombre de usuario ya existe en la base de datos
        while (userRepository.existsByNameAndLastName(firstInitial, fullLastName)) {
            // Si existe, agrega un sufijo aleatorio para hacerlo único
            int randomSuffix = (int) (Math.random() * 1000); // Genera un número aleatorio entre 0 y 999
            generatedUsername = firstInitial + fullLastName + moduleId + randomSuffix;
        }

        return generatedUsername;
    }

}
