package config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import auth.AuthenticationFailureHandler;
import auth.AuthenticationSuccessHandler;
import auth.LogoutSuccess;
import auth.RestAuthenticationEntryPoint;
import serviceImpl.CustomUserDetailsService;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {

	protected final Log LOGGER = LogFactory.getLog(getClass());

	private final CustomUserDetailsService jwtUserDetailsService;
	private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;
	private final LogoutSuccess logoutSuccess;
	private final AuthenticationSuccessHandler authenticationSuccessHandler;
	private final AuthenticationFailureHandler authenticationFailureHandler;

	@Autowired
	public WebSecurityConfig(CustomUserDetailsService jwtUserDetailsService,
			RestAuthenticationEntryPoint restAuthenticationEntryPoint, LogoutSuccess logoutSuccess,
			AuthenticationSuccessHandler authenticationSuccessHandler,
			AuthenticationFailureHandler authenticationFailureHandler) {
		super();
		this.jwtUserDetailsService = jwtUserDetailsService;
		this.restAuthenticationEntryPoint = restAuthenticationEntryPoint;
		this.logoutSuccess = logoutSuccess;
		this.authenticationSuccessHandler = authenticationSuccessHandler;
		this.authenticationFailureHandler = authenticationFailureHandler;
	}

	@Value("${jwt.cookie}")
	private String TOKEN_COOKIE;

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
