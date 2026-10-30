
package com.app.service;

import com.app.dto.request.LoginRequestDto;
import com.app.dto.response.LoginResponseDto;

public interface AuthService {

    LoginResponseDto login(LoginRequestDto loginRequestDto);

    void forgotPassword(String email);

    void resetPassword(String token, String newPassword);
}
