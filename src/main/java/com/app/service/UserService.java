
package com.app.service;

import java.util.List;
import java.util.Optional;

import com.app.dto.request.ProfileUpdateDto;
import com.app.dto.request.UserRequestDto;
import com.app.entity.User;

public interface UserService {

    User registerUser(UserRequestDto userRequestDto);

    List<User> getAllUsers();

    Optional<User> getUserById(Long id);

    User getMyProfile();

    User updateMyProfile(ProfileUpdateDto profileUpdateDto);
}
