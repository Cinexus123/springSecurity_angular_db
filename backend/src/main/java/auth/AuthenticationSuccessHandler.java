package auth;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import entity.User;
import entity.UserToken;
import security.TokenHelper;

public class AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	private final TokenHelper tokenHelper;
	private final ObjectMapper objectMapper;
	@Value("${jwt.expires_in}")
	private int EXPIRES_IN;
	@Value("${jwt.cookie}")
	private String TOKEN_COOKIE;

	@Autowired
	public AuthenticationSuccessHandler(TokenHelper tokenHelper, ObjectMapper objectMapper) {
		super();
		this.tokenHelper = tokenHelper;
		this.objectMapper = objectMapper;
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		clearAuthenticationAttributes(request);
		User user = (User) authentication.getPrincipal();

		String jws = tokenHelper.generateToken(user.getUsername());

		
		Cookie authCookie = new Cookie(TOKEN_COOKIE, (jws));

		authCookie.setHttpOnly(true);

		authCookie.setMaxAge(EXPIRES_IN);

		authCookie.setPath("/");
		
		response.addCookie(authCookie);

		
		UserToken userTokenState = new UserToken(jws, EXPIRES_IN);
		String jwtResponse = objectMapper.writeValueAsString(userTokenState);
		response.setContentType("application/json");
		response.getWriter().write(jwtResponse);
	}

}
