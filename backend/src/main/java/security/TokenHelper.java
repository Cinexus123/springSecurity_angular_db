package security;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.joda.time.DateTime;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class TokenHelper {
	
	private final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;
	
	@Autowired
	@Qualifier("customUserDetailsService")
	private UserDetailsService userDetailsService;
	@Value("${app.name}")
	private String APP_NAME;
	@Value("${jwt.secret}")
	private String SECRET;
	@Value("${jwt.expires_in}")
	private int EXPIRES_IN;
	@Value("${jwt.header}")
	private String AUTH_HEADER;
	@Value("${jwt.cookie}")
	private String AUTH_COOKIE;
	
	
	private String getUsernameFromToken(String token) {
		 String username;
	        try {
	            final Claims claims = this.getClaimsFromToken(token);
	            username = claims.getSubject();
	        } catch (Exception e) {
	            username = null;
	        }
	        return username;
	}
	
	public String getToken(HttpServletRequest request) {
		 Cookie authCookie = getCookieValueByName(request, AUTH_COOKIE);
	        if (authCookie != null) {
	            return authCookie.getValue();
	        }
	        /**
	         *  Getting the token from Authentication header
	         *  e.g Bearer your_token
	         */
	        String authHeader = request.getHeader(AUTH_HEADER);
	        if (authHeader != null && authHeader.startsWith("Bearer ")) {
	            return authHeader.substring(7);
	        }

	        return null;
	}

	private Cookie getCookieValueByName(HttpServletRequest request, String name) {
		if (request.getCookies() == null) {
            return null;
        }
		for (int i = 0; i < request.getCookies().length; i++) {
	        if (request.getCookies()[i].getName().equals(name)) {
	            return request.getCookies()[i];
	            }
	        }
		return null;
	}

	public boolean canTokenBeRefreshed(String token) {
		 try {
	            final Date expirationDate = getClaimsFromToken(token).getExpiration();
	            String username = getUsernameFromToken(token);
	            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
	            return expirationDate.compareTo(generateCurrentDate()) > 0;
	        } catch (Exception e) {
	            return false;
	        }
	}

	private Date generateCurrentDate() {
		return new Date(getCurrentTimeMillis());
	}

	private long getCurrentTimeMillis() {
		return DateTime.now().getMillis();
	}

	private Claims getClaimsFromToken(String token) {
		Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(this.SECRET)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
	}

	public String refreshToken(String token) {
		 String refreshedToken;
	        try {
	            final Claims claims = getClaimsFromToken(token);
	            claims.setIssuedAt(generateCurrentDate());
	            refreshedToken = generateToken(claims);
	        } catch (Exception e) {
	            refreshedToken = null;
	        }
	        return refreshedToken;
	}

    String generateToken(Map<String, Object> claims) {
    	 return Jwts.builder()
                 .setClaims(claims)
                 .setExpiration(generateExpirationDate())
                 .signWith(SIGNATURE_ALGORITHM, SECRET)
                 .compact();
	}

	private Date generateExpirationDate() {
		return new Date(getCurrentTimeMillis() + this.EXPIRES_IN * 1000);
	}

	public String generateToken(String username) {
		 return Jwts.builder()
	                .setIssuer(APP_NAME)
	                .setSubject(username)
	                .setIssuedAt(generateCurrentDate())
	                .setExpiration(generateExpirationDate())
	                .signWith(SIGNATURE_ALGORITHM, SECRET)
	                .compact();
	}

}