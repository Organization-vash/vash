package com.vash.entel.service;

import com.vash.entel.model.entity.User;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {
    User getAuthenticatedUser(HttpServletRequest request);
}
