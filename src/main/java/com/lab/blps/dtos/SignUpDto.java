package com.lab.blps.dtos;

import com.lab.blps.models.applications.Role;
import lombok.Data;

@Data
public class SignUpDto {
    private String username;
    private String password;
    private Role role;
    private String email;
}
