
package com.app.service.impl;

import java.util.List;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.app.dto.request.ProfileUpdateDto;
import com.app.dto.request.UserRequestDto;
import com.app.entity.User;
import com.app.enums.Role;
import com.app.exception.DuplicateEmailException;
import com.app.repository.UserRepository;
import com.app.service.UserService;

import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

    @Override
    public User registerUser(UserRequestDto userRequestDto) {

        if (userRepository.findByEmail(userRequestDto.getEmail()).isPresent()) {
        	throw new DuplicateEmailException("Email already registered");
        }

        User user = modelMapper.map(userRequestDto, User.class);

        user.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
        user.setRole(Role.CUSTOMER);

        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }
    

@Override
@Transactional(readOnly = true)
public User getMyProfile() {
    return getLoggedInUser();
}

@Override
@Transactional
public User updateMyProfile(ProfileUpdateDto dto) {

    User user = getLoggedInUser();

    Optional<User> existingUser =
            userRepository.findByEmail(dto.getEmail());

    if (existingUser.isPresent()
            && !existingUser.get().getId().equals(user.getId())) {

        throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "Email is already registered");
    }

    user.setName(dto.getName());
    user.setEmail(dto.getEmail());
    user.setPhone(dto.getPhone());

    User updatedUser = userRepository.save(user);

    return updatedUser;
}

private User getLoggedInUser() {

    String email = SecurityContextHolder.getContext()
            .getAuthentication()
            .getName();

    return userRepository.findByEmail(email)
            .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "User not found"));
}

}
