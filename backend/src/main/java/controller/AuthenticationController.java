package controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import config.WebSecurityConfig;
import entity.UserToken;
import security.TokenHelper;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthenticationController {
	
	private final TokenHelper tokenHelper;
	private final WebSecurityConfig userDetailsService;
	    
	@Value("${jwt.expires_in}")
    private int EXPIRES_IN;

    @Value("${jwt.cookie}")
    private String TOKEN_COOKIE;
    
    @Autowired
    public AuthenticationController(TokenHelper tokenHelper, WebSecurityConfig userDetailsService) {
        this.tokenHelper = tokenHelper;
        this.userDetailsService = userDetailsService;
    }
    
    @RequestMapping(value = "/refresh", method = RequestMethod.GET)
    public ResponseEntity<?> refreshAuthenticationToken(HttpServletRequest request, HttpServletResponse response)
    {
    	String authToken = tokenHelper.getToken(request);
    	if (authToken != null && tokenHelper.canTokenBeRefreshed(authToken)) {
    		String refreshedToken = tokenHelper.refreshToken(authToken);
    		Cookie authCookie = new Cookie(TOKEN_COOKIE, (refreshedToken));
    		authCookie.setPath("/");
            authCookie.setHttpOnly(true);
            authCookie.setMaxAge(EXPIRES_IN);
            // Add cookie to response
            response.addCookie(authCookie);
            
            UserToken userToken = new UserToken(refreshedToken, EXPIRES_IN);
            return ResponseEntity.ok(userToken);
    	} else {
            UserToken userToken = new UserToken();
            return ResponseEntity.accepted().body(userToken);
        }
    	
    }
    
    @RequestMapping(value = "/changePassword", method = RequestMethod.POST)
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> changePassword(@RequestBody PasswordChanger passwordChanger) throws Exception {
        userDetailsService.changePassword(passwordChanger.oldPassword, passwordChanger.newPassword);
        Map<String, String> result = new HashMap<>();
        result.put("result", "success");
        return ResponseEntity.accepted().body(result);
    }

    static class PasswordChanger {
        public String oldPassword;
        public String newPassword;
    }

}
