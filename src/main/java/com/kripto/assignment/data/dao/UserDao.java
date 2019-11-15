package com.kripto.assignment.data.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kripto.assignment.data.model.User;

@Repository
public interface UserDao extends JpaRepository<User, Long> {
    User findByEmail(String email);
}