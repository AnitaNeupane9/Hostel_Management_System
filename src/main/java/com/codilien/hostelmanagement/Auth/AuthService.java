package com.codilien.hostelmanagement.Auth;

import com.codilien.hostelmanagement.Enum.Role;
import jakarta.servlet.http.HttpSession;

public interface AuthService {
    public boolean login(String email, String password, Role role, HttpSession session);
    public void logout(HttpSession Session );
}
