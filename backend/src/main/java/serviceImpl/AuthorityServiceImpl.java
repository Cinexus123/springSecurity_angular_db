package serviceImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import entity.Authority;
import entity.UserRoleName;
import repository.AuthorityRepository;
import service.AuthorityService;

public class AuthorityServiceImpl implements AuthorityService {

	private final AuthorityRepository authorityRepository;

	@Autowired
	public AuthorityServiceImpl(AuthorityRepository authorityRepository) {
		this.authorityRepository = authorityRepository;
	}

	@Override
	public List<Authority> findById(Long id) {
		 Authority auth = this.authorityRepository.getOne(id);
	        List<Authority> auths = new ArrayList<>();
	        auths.add(auth);
	        return auths;
	}

	@Override
	public List<Authority> findByName(UserRoleName name) {
		Authority auth = this.authorityRepository.findByName(name);
        List<Authority> auths = new ArrayList<>();
        auths.add(auth);
        return auths;
	}

}
