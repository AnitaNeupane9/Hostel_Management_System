package com.codilien.hostelmanagement.Auth;


import lombok.Getter;

@Getter
public class LoginRequest {
    private String email;
    private String password;
}
