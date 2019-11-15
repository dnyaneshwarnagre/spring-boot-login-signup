package com.kripto.assignment.service.impl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.kripto.assignment.data.dao.RoleDao;
import com.kripto.assignment.data.dao.UserDao;
import com.kripto.assignment.data.model.Role;
import com.kripto.assignment.data.model.User;
import com.kripto.assignment.service.interf.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
    private UserDao userDao;
	
	@Autowired
    private RoleDao roleDao;
	
	@Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Override
	public User findUserByEmail(String email) {
		  return userDao.findByEmail(email);
	}

	@Override
	public User saveUser(User user) {
		return userDao.save(user);
	}

	@Override
	public List<User> list() {
		return userDao.findAll();
	}

	@Override
	public User create(User user) {
    	user.setActive(1);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        
        //Set ADMIN role to user
        if(user.getEmail().endsWith("@test.com")) {
        	  Role userRole = roleDao.findByRole("ADMIN");
              user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
        }else {
        	  Role userRole = roleDao.findByRole("USER");
              user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
        }
        return saveUser(user);
        
	}

	@Override
	public User update(User user, User updateUser) {
		updateUser.setEducation(user.getEducation());
		updateUser.setAddress(user.getAddress());
		updateUser.setCompany(user.getCompany());
		updateUser.setPhone(user.getPhone());
		return saveUser(updateUser);

	}

}
