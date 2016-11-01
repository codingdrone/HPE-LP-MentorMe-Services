package com.livingprogress.mentorme.security;


import com.livingprogress.mentorme.entities.SearchResult;
import com.livingprogress.mentorme.entities.User;
import com.livingprogress.mentorme.entities.UserRole;
import com.livingprogress.mentorme.entities.UserSearchCriteria;
import com.livingprogress.mentorme.entities.UserStatus;
import com.livingprogress.mentorme.exceptions.ConfigurationException;
import com.livingprogress.mentorme.services.UserService;
import com.livingprogress.mentorme.utils.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The simple implementation of the UserDetailsService.
 */
@Service
public class SimpleUserDetailsService implements UserDetailsService {
    /**
     * The UserService to load user by username.
     */
    @Autowired
    private UserService userService;

    /**
     * Check if all required fields are initialized properly.
     *
     * @throws ConfigurationException if any required field is not initialized properly.
     */
    @PostConstruct
    protected void checkConfiguration() {
        Helper.checkConfigNotNull(userService, "userService");
    }

    /**
     * Locates the user based on the username.
     *
     * @param username the username
     * @return the UserDetails
     * @throws UsernameNotFoundException if there is no match or invalid user found.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            UserSearchCriteria criteria = new com.livingprogress.mentorme
                    .entities.UserSearchCriteria();
            criteria.setUsername(username);
            SearchResult<User> users =
                    userService.search(criteria, null);
            if (users.getEntities()
                     .isEmpty()) {
                throw new UsernameNotFoundException(
                        String.format("The user with name [%s] can not be found.", username));
            }
            User user = users.getEntities()
                             .get(0);
            if (user.getRoles()
                    .isEmpty()) {
                throw new UsernameNotFoundException(
                        String.format("The user with name [%s] does not have roles defined.", username));
            }
            List<GrantedAuthority> authorities = buildUserAuthority(user.getRoles());
            return buildUserForAuthentication(user, authorities);
        }catch (UsernameNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new UsernameNotFoundException("Failed to get user data.", e);
        }
    }

    /**
     * Build the user authority.
     *
     * @param roles the roles
     * @return the authority
     */
    private List<GrantedAuthority> buildUserAuthority(List<UserRole> roles) {
        Set<GrantedAuthority> auths = new HashSet<>();
        for (UserRole role : roles) {
            auths.add(new SimpleGrantedAuthority(role.getValue()));
        }
        return new ArrayList<>(auths);
    }

    /**
     * Build the user details entity.
     *
     * @param user the user
     * @param authorities the authorities
     * @return user details entity
     */
    private UserDetails buildUserForAuthentication(User user, List<GrantedAuthority> authorities) {
        // will check active/inactive status here
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getPassword(), UserStatus.ACTIVE.equals(user.getStatus()), true, true, true, authorities);
    }
}
