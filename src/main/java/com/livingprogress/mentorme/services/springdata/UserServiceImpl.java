package com.livingprogress.mentorme.services.springdata;

import com.livingprogress.mentorme.entities.ForgotPassword;
import com.livingprogress.mentorme.entities.NewPassword;
import com.livingprogress.mentorme.entities.User;
import com.livingprogress.mentorme.entities.UserSearchCriteria;
import com.livingprogress.mentorme.exceptions.AccessDeniedException;
import com.livingprogress.mentorme.exceptions.ConfigurationException;
import com.livingprogress.mentorme.exceptions.EntityNotFoundException;
import com.livingprogress.mentorme.exceptions.MentorMeException;
import com.livingprogress.mentorme.services.UserService;
import com.livingprogress.mentorme.utils.Helper;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.UUID;

/**
 * The Spring Data JPA implementation of UserService, extends BaseService<User,UserSearchCriteria>. Effectively thread safe after configuration.
 */
@Service
@NoArgsConstructor
public class UserServiceImpl extends BaseService<User, UserSearchCriteria> implements UserService {
    /**
     * The expiration time in millis.
     */
    @Value("${forgotPassword.expirationTimeInMillis}")
    private long forgotPasswordExpirationTimeInMillis;

    /**
     * The maxTimes to send forgot password request for single user.
     */
    @Value("${forgotPassword.maxTimes}")
    private long forgotPasswordMaxTimes;


    /**
     * The forgot password repository for CRUD operations. Should be non-null after injection.
     */
    @Autowired
    private ForgotPasswordRepository forgotPasswordRepository;

    /**
     * The forgot password repository for CRUD operations. Should be non-null after injection.
     */
    @Autowired
    private UserRepository userRepository;

    /**
     * Check if all required fields are initialized properly.
     *
     * @throws ConfigurationException if any required field is not initialized properly.
     */
    @PostConstruct
    protected void checkConfiguration() {
        super.checkConfiguration();
        Helper.checkConfigNotNull(forgotPasswordRepository, "forgotPasswordRepository");
        Helper.checkConfigNotNull(userRepository, "userRepository");
        Helper.checkConfigPositive(forgotPasswordExpirationTimeInMillis, "forgotPasswordExpirationTimeInMillis");
        Helper.checkConfigPositive(forgotPasswordMaxTimes, "forgotPasswordMaxTimes");
    }

    /**
     * This method is used to get the specification.
     *
     * @param criteria the search criteria
     * @return the specification
     * @throws MentorMeException if any other error occurred during operation
     */
    protected Specification<User> getSpecification(UserSearchCriteria criteria) throws MentorMeException {
        return new UserSpecification(criteria);
    }

    /**
     * This method is used to create an entity.
     *
     * @param entity the entity to create
     * @return the created entity
     * @throws IllegalArgumentException if entity is null or not valid
     * @throws MentorMeException if any other error occurred during operation
     */
    @Transactional
    public User create(User entity) throws MentorMeException {
        Helper.encodePassword(entity, false);
        return super.create(entity);
    }

    /**
     * This method is used to update an entity.
     *
     * @param id the id of the entity to update
     * @param entity the entity to update
     * @return the updated entity
     * @throws IllegalArgumentException if id is not positive or entity is null or id of entity is not positive
     * or id of  entity not match id or entity is invalid
     * @throws EntityNotFoundException if the entity does not exist
     * @throws MentorMeException if any other error occurred during operation
     */
    @Transactional
    public User update(long id, User entity) throws MentorMeException {
        User existing = super.checkUpdate(id, entity);
        if (Helper.isUpdated(existing, entity)) {
            return getRepository().save(existing);
        }
        return existing;
    }

    /**
     * This method is used to get the user by provider id and provider user id.
     *
     * @param providerId the provider id
     * @param providerUserId the provider user id
     * @return the match user
     * @throws IllegalArgumentException: if parameters are null or not valid
     * @throws MentorMeException: if any other error occurred during operation
     */
    public User findByProviderIdAndProviderUserId(String providerId, String providerUserId) throws MentorMeException {
        Helper.checkNullOrEmpty(providerId, "providerId");
        Helper.checkNullOrEmpty(providerUserId, "providerUserId");
        return userRepository.findByProviderIdAndProviderUserId(providerId, providerUserId);
    }

    /**
     * This method is used to create the forgot password entity for the given user.
     *
     * @param userId the user id.
     * @return the created forgot password entity
     * @throws IllegalArgumentException if id is not positive
     * @throws EntityNotFoundException if the entity does not exist
     * @throws MentorMeException if any other error occurred during operation
     */
    @Transactional
    public ForgotPassword forgotPassword(long userId) throws MentorMeException {
        Helper.checkPositive(userId, "userId");
        long count = forgotPasswordRepository.countByUserId(userId);
        if (count > forgotPasswordMaxTimes) {
            throw new AccessDeniedException("Reach max times to send forgot password request!");
        }
        ForgotPassword forgotPassword = new ForgotPassword();
        String token = UUID.randomUUID().toString();
        Date expiredOn = new Date(System.currentTimeMillis() + forgotPasswordExpirationTimeInMillis);
        forgotPassword.setUserId(userId);
        forgotPassword.setToken(token);
        forgotPassword.setExpiredOn(expiredOn);
        return forgotPasswordRepository.save(forgotPassword);
    }

    /**
     * This method is used to update the forgot password entity for the given token.
     *
     * @param newPassword the newPassword request.
     * @return true if update the password successfully otherwise false
     * @throws IllegalArgumentException if newPassword is null or invalid
     * @throws MentorMeException if any other error occurred during operation
     */
    @Transactional
    public boolean updatePassword(NewPassword newPassword) throws MentorMeException {
        Helper.checkNull(newPassword, "newPassword");
        String token = newPassword.getToken();
        String newPass = newPassword.getNewPassword();
        Helper.checkNullOrEmpty(token, "newPassword.token");
        Helper.checkNullOrEmpty(newPass, "newPassword.newPassword");
        ForgotPassword forgotPassword = forgotPasswordRepository.findByToken(token);
        if (forgotPassword != null) {
            Date currentDate = new Date();
            if (currentDate.before(forgotPassword.getExpiredOn())) {
                User user = get(forgotPassword.getUserId());
                Helper.encodePassword(user, false);
                getRepository().save(user);
                forgotPasswordRepository.delete(forgotPassword);
                return true;
            }
        }
        return false;
    }
}

