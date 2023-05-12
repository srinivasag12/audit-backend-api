package com.api.central.User;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

@Service
public class UserManager {

	private UserRepository userRepository;	

	@Autowired
	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public UserDetail findUser(String name) {		
		return userRepository.findUser(name);
	}

}
