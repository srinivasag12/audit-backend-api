package com.api.central.User;


public interface UserRepository {
	public UserDetail findUser(String name);
}
