package config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class WebSecurityConfig {

	protected final Log LOGGER = LogFactory.getLog(getClass());
	
	public void changePassword(String oldPassword, String newPassword) throws Exception {
		 Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
		    String username = currentUser.getName();

		    if (authenticationManagerBean() != null) {
		      LOGGER.debug("Re-authenticating user '" + username + "' for password change request.");

		      authenticationManagerBean().authenticate(new UsernamePasswordAuthenticationToken(username, oldPassword));
		    } else {
		      LOGGER.debug("No authentication manager set. can't change Password!");

		      return;
		    }

		    LOGGER.debug("Changing password for user '" + username + "'");

		    User user = jwtUserDetailsService.loadUserByUsername(username);

		    user.setPassword(new BCryptPasswordEncoder().encode(newPassword));
		    jwtUserDetailsService.save(user);
		
	}

}
