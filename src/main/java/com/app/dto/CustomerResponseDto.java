
package com.app.dto;

import java.time.LocalDateTime;

import com.app.enums.Role;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CustomerResponseDto {

    private Long id;
    private String name;
    private String email;
    private String phone;
    private Role role;
    private LocalDateTime createdAt;
}
