package com.livingprogress.mentorme.security;

import com.livingprogress.mentorme.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

/**
 * The token auth service.
 */
@Service
public class TokenAuthenticationService {
    /**
     * The token auth header name.
     */
    private static final String AUTH_HEADER_NAME = "X-AUTH-TOKEN";

    /**
     * The auth cookie name.
     */
    private static final String AUTH_COOKIE_NAME = "AUTH-TOKEN";
    /**
     * The milliseconds for 10 days.
     */
    private static long TEN_DAYS = 1000 * 60 * 60 * 24 * 10;

    /**
     * The token handler.
     */
    private final TokenHandler tokenHandler;

    /**
     * The token auth service constructor.
     *
     * @param secret the secret
     */
    @Autowired
    private TokenAuthenticationService(@Value("${token.secret}") String secret) {
        tokenHandler = new TokenHandler(DatatypeConverter.parseBase64Binary(secret));
    }

    /**
     * Add authentication.
     *
     * @param response the servlet response.
     * @param authentication the user auth object.
     */
    public void addAuthentication(HttpServletResponse response, UserAuthentication authentication) {
        final User user = (User) authentication.getDetails();
        user.setExpires(System.currentTimeMillis() + TEN_DAYS);
        final String token = tokenHandler.createTokenForUser(user);
        // Put the token into a cookie because the client can't capture response
        // headers of redirects / full page reloads.
        // (Its reloaded as a result of this response triggering a redirect back to "/")
        response.addCookie(createCookieForToken(token));
    }


    /**
     * Get user auth object from request.
     *
     * @param request the servlet request.
     * @return the user auth request.
     */
    public UserAuthentication getAuthentication(HttpServletRequest request) {
        // to prevent CSRF attacks we still only allow authentication using a custom HTTP header
        // (it is up to the client to read our previously set cookie and put it in the header)
        final String token = request.getHeader(AUTH_HEADER_NAME);
        if (token != null) {
            final User user = tokenHandler.parseUserFromToken(token);
            if (user != null) {
                return new UserAuthentication(user);
            }
        }
        return null;
    }

    /**
     * Create cookie for token.
     *
     * @param token the token.
     * @return cookie with auth token
     */
    private Cookie createCookieForToken(String token) {
        final Cookie authCookie = new Cookie(AUTH_COOKIE_NAME, token);
        authCookie.setPath("/");
        return authCookie;
    }
}
