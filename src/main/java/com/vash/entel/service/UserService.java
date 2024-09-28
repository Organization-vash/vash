package com.vash.entel.service;

import com.vash.entel.dto.UserDTO;
import com.vash.entel.model.entity.User;

import java.util.List;

public interface UserService {
    List<UserDTO> getAll();
    UserDTO createUser(UserDTO userDTO);
    UserDTO findById(Integer id);
    UserDTO updateUser(Integer id, UserDTO userDTO);
    void delete(Integer id);
    void validateDocument(UserDTO userDTO);
}
