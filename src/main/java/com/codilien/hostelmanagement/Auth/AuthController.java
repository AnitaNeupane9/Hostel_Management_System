package com.codilien.hostelmanagement.Auth;

import com.codilien.hostelmanagement.Enum.Role;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest, HttpSession session) {

        // checking if it's a student login
        boolean isStudent = authService.login(loginRequest.getEmail(), loginRequest.getPassword(), Role.STUDENT, session);
        if (isStudent) {
            return ResponseEntity.ok("Login successful as Student");
        }

        // Checking for employee roles (ADMIN, WARDEN, ACCOUNTANT)
        for (Role role : Role.values()) {
            if (role != Role.STUDENT) {
                boolean isEmployee = authService.login(loginRequest.getEmail(), loginRequest.getPassword(), role, session);
                if (isEmployee) {

                    return ResponseEntity.ok("Login successful as " + role.name());
                }
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password.");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logOut(HttpSession session){
        authService.logout(session);
        return ResponseEntity.status(HttpStatus.OK).body("Logout Successfully!");
    }
}
