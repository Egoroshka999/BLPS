package com.lab.blps.dtos;

import com.lab.blps.models.applications.User;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationDto {
    private String token;
    private User user;
}
