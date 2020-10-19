package service;

import java.util.List;

import entity.Authority;
import entity.UserRoleName;

public interface AuthorityService {

	List<Authority> findById(Long id);
	
	List<Authority> findByName(UserRoleName name);
}
