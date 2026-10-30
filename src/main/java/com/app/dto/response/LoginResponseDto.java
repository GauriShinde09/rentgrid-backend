
package com.app.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginResponseDto {

    private String token;
    private String message;
    private String role;
    private String name;
    private String email;

    public LoginResponseDto(
            String token,
            String message,
            String role,
            String name,
            String email) {

        this.token = token;
        this.message = message;
        this.role = role;
        this.name = name;
        this.email = email;
    }
}
