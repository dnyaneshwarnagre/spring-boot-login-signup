package com.kripto.assignment.service.interf;

import java.util.List;

import com.kripto.assignment.data.model.User;

public interface UserService {

	User findUserByEmail(String email);
	
	User saveUser(User user);
	
	List<User> list();
	
	User create(User user);
	
	User update(User user, User updateUser);
}
