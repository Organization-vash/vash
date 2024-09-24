package com.vash.entel.service.impl;

import com.vash.entel.dto.UserDTO;
import com.vash.entel.exception.BadRequestException;
import com.vash.entel.exception.ResourceNotFoundException;
import com.vash.entel.mapper.UserMapper;
import com.vash.entel.model.entity.User;
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
        user.setDocument(userDTO.getDocument());
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
}
