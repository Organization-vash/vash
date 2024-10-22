package com.vash.entel.service.impl;

import com.vash.entel.dto.UserDTO;
import com.vash.entel.exception.BadRequestException;
import com.vash.entel.exception.ResourceNotFoundException;
import com.vash.entel.mapper.UserMapper;
import com.vash.entel.model.entity.User;
import com.vash.entel.model.enums.DocumentType;
import com.vash.entel.repository.UserRepository;
import com.vash.entel.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

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

        userRepository.findByModuleId(userDTO.getModuleId())
                .ifPresent(existingUser -> {
                    throw new BadRequestException("El módulo ya está siendo utilizado");
                });

        validateDocument(userDTO);
        userRepository.findByNameAndLastName(userDTO.getName(), userDTO.getLastName())
                .ifPresent(existingUser -> {
                    throw new BadRequestException("El usuario ya existe con el mismo nombre y apellido");
                });
        userRepository.findByEmail(userDTO.getEmail())
                .ifPresent(existingUser -> {
                    throw new BadRequestException("El email ya existe");
                });
        userRepository.findByNumberDoc(userDTO.getNumberDoc())
                .ifPresent(existingUser -> {
                    throw new BadRequestException("El documento ya existe");
                });

        String username = generateUsername(userDTO.getName(), userDTO.getLastName(), userDTO.getModuleId());
        userDTO.setUsername(username);
        User user = userMapper.toUserEntity(userDTO);
        user.setCreatedAt(LocalDateTime.now());
        User savedUser = userRepository.save(user);

        return userMapper.toUserDTO(savedUser);
    }

    @Transactional
    @Override
    public UserDTO findById(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("El usuario con ID "+id+" no fue encontrado"));
        return userMapper.toUserDTO(user);
    }

    @Transactional
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Transactional
    @Override
    public UserDTO updateUser(Integer id, UserDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("El usuario con ID " + id + " no fue encontrado"));
        userRepository.findByNameAndLastName(userDTO.getName(), userDTO.getLastName())
                .ifPresent(existingUser -> {
                    throw new BadRequestException("Ya existe un usuario con el mismo nombre y apellido");
                });
        userRepository.findByNumberDoc(userDTO.getNumberDoc())
                .ifPresent(existingUser -> {
                    throw new BadRequestException("Ya existe un usuario con el mismo número de documento");
                });

        user.setName(userDTO.getName());
        user.setLastName(userDTO.getLastName());
        user.setDocumentType(userDTO.getDocumentType());
        user.setNumberDoc(userDTO.getNumberDoc());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setRole(userDTO.getRole());

        user.setUpdatedAt(LocalDateTime.now());

        User updatedUser = userRepository.save(user);
        return userMapper.toUserDTO(updatedUser);
    }

    @Transactional
    @Override
    public void delete(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("El usuario con ID " + id + " no fue encontrado"));
        userRepository.delete(user);
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

    public String generateUsername(String firstName, String lastName, Integer moduleId) {
        // Get the first letter of the first name and convert it to uppercase
        String firstInitial = firstName.substring(0, 1).toUpperCase();

        String fullLastName = lastName.toUpperCase();

        return firstInitial + fullLastName + moduleId;
    }

    @Transactional
    @Override
    public List<UserDTO> findByNumberDocOrName(Integer numberDoc, String name) {
        List<User> users = userRepository.findByNumberDocOrName(numberDoc, name);
        return users.stream()
                .map(userMapper::toUserDTO)
                .toList();
    }
}