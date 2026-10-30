
package com.app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.app.entity.User;
import com.app.enums.Role;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);


    // Find users by role (ADMIN or CUSTOMER)
    List<User> findByRole(Role role);

    // Search users by name, ignoring uppercase/lowercase
    List<User> findByNameContainingIgnoreCase(String name);
    Page<User> findByRole(Role role, Pageable pageable);

}
