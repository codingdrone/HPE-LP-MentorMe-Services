package com.livingprogress.mentorme.controllers;

import com.livingprogress.mentorme.entities.SearchResult;
import com.livingprogress.mentorme.entities.User;
import com.livingprogress.mentorme.entities.UserSearchCriteria;
import com.livingprogress.mentorme.exceptions.AccessDeniedException;
import com.livingprogress.mentorme.exceptions.ConfigurationException;
import com.livingprogress.mentorme.exceptions.MentorMeException;
import com.livingprogress.mentorme.security.TokenHandler;
import com.livingprogress.mentorme.services.UserService;
import com.livingprogress.mentorme.utils.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.xml.bind.DatatypeConverter;

/**
 * The login REST controller. Is effectively thread safe.
 */
@RestController
@RequestMapping("/login")
public class LoginController {

    /**
     * The user service used to perform operations. Should be non-null after injection.
     */
    @Autowired
    private UserService userService;

    /**
     * The token expires milliseconds for 10 days.
     */
    @Value("${token.expirationTimeInMillis}")
    private long tokenExpirationTimeInMillis;


    /**
     * The token handler.
     */
    private final TokenHandler tokenHandler;

    /**
     * The login controller constructor.
     */
    @Autowired
    public LoginController(@Value("${token.secret}") String secret) {
        tokenHandler = new TokenHandler(DatatypeConverter.parseBase64Binary(secret));
    }

    /**
     * Check if all required fields are initialized properly.
     *
     * @throws ConfigurationException if any required field is not initialized properly.
     */
    @PostConstruct
    protected void checkConfiguration() {
        Helper.checkConfigNotNull(userService, "userService");
        Helper.checkConfigNotNull(tokenHandler, "tokenHandler");
        Helper.checkConfigPositive(tokenExpirationTimeInMillis, "tokenExpirationTimeInMillis");
    }

    /**
     * This method is used to login with basic auth and return JWT token with expired time.
     *
     * @return the JWT token
     * @throws AccessDeniedException if does not allow to perform action
     * @throws MentorMeException if any other error occurred during operation
     */
    @RequestMapping(method = RequestMethod.POST)
    public String login() throws MentorMeException {
        Authentication authentication = SecurityContextHolder.getContext()
                                                             .getAuthentication();
        UserSearchCriteria criteria = new UserSearchCriteria();
        criteria.setUsername(authentication.getName());
        SearchResult<User>  users = userService.search(criteria, null);
        // validate valid user exists in SimpleUserDetailsService already
        User user = users.getEntities().get(0);
        long expires = System.currentTimeMillis() + tokenExpirationTimeInMillis;
        user.setExpires(expires);
        return tokenHandler.createTokenForUser(user);
    }
}
