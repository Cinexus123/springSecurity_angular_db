package service;

import java.util.List;

import entity.User;
import entity.UserRequest;

public interface UserService {

	User findById(Long id);

	User findByUsername(String username);

	List<User> findAll();

	User save(UserRequest user);
	
	void resetCredentials();
}
