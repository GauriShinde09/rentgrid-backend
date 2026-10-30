package com.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.entity.SupportRequest;

public interface SupportRepository extends JpaRepository<SupportRequest, Long> {

    // Get all support requests of a particular customer
    List<SupportRequest> findByUserId(Long userId);

}