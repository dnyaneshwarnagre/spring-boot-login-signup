package com.kripto.assignment.data.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kripto.assignment.data.model.Role;

@Repository
public interface RoleDao extends JpaRepository<Role, Integer> {
    Role findByRole(String role);

}
