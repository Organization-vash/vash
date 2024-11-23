package com.vash.entel.service;

import com.vash.entel.dto.AuthResponseDTO;
import com.vash.entel.dto.LoginDTO;
import com.vash.entel.dto.UserDTO;

import java.util.List;

public interface UserService {
    List<UserDTO> getAll();
    UserDTO createUser(UserDTO userDTO);
    UserDTO findById(Integer id);
    UserDTO updateUser(Integer id, UserDTO userDTO);
    void validateDocument(UserDTO userDTO);
    String generateUsername(String firstName, String lastName, Integer moduleId);

    List<UserDTO> findByNumberDocOrName(Integer numberDoc, String name);
    AuthResponseDTO login(LoginDTO loginDTO);
    void delete(Integer id);
}
