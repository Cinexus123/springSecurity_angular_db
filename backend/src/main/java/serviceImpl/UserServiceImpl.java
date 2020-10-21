package serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import entity.Authority;
import entity.User;
import entity.UserRequest;
import entity.UserRoleName;
import repository.UserRepository;
import service.AuthorityService;
import service.UserService;

@Service
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final AuthorityService authService;

	@Autowired
	public UserServiceImpl(UserRepository userRepository, AuthorityService authService) {
		super();
		this.userRepository = userRepository;
		this.authService = authService;
	}

	@Override
	@PreAuthorize("hasRole('ADMIN')")
	public User findById(Long id) throws AccessDeniedException {
		return userRepository.getOne(id);
	}

	@Override
	public User findByUsername(String username) throws UsernameNotFoundException {
		return userRepository.findByUsername(username).orElse(null);
	}

	@PreAuthorize("hasRole('ADMIN')")
	public List<User> findAll() throws AccessDeniedException {
		return userRepository.findAll();
	}

	@Override
	public User save(UserRequest userRequest) {
		User user = new User();
		user.setUsername(userRequest.getUsername());
		user.setPassword(getBCryptPasswordEncoder().encode(userRequest.getPassword()));
		user.setFirstname(userRequest.getFirstname());
		user.setLastname(userRequest.getLastname());
		List<Authority> auth = authService.findByName(UserRoleName.ROLE_USER);
		user.setAuthorities(auth);
		return userRepository.save(user);
	}

	private BCryptPasswordEncoder getBCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	public void resetCredentials() {
		List<User> users = userRepository.findAll();
		for (User user : users) {
			user.setPassword(getBCryptPasswordEncoder().encode("123"));
			userRepository.save(user);
		}

	}

}