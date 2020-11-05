package com.bfwg.service;

import com.bfwg.model.User;
import com.bfwg.model.UserRequest;

import java.util.List;

public interface UserService {
  void resetCredentials();

  User findById(Long id);

  User findByUsername(String username);

  List<User> findAll();

  User save(UserRequest user);
}
